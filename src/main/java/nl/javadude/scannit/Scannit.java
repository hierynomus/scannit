/* License added by: GRADLE-LICENSE-PLUGIN
 *
 *    Copyright 2011 Jeroen van Erp (jeroen@javadude.nl)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package nl.javadude.scannit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.Monitor;

import nl.javadude.scannit.registry.Registry;
import nl.javadude.scannit.registry.RegistryHelper;

public class Scannit {
    private static final AtomicReference<Scannit> REF = new AtomicReference<Scannit>();
    private static final Monitor M = new Monitor();

    final Registry registry = new Registry();
    private RegistryHelper registryHelper;

    public Scannit(Configuration configuration) {
        configuration.wireScanners();
        new Worker(configuration, registry).scan();
        registryHelper = new RegistryHelper(registry);
    }

    public static Scannit boot(Configuration configuration) {
        return boot(configuration, false);
    }

    public static Scannit boot(Configuration configuration, boolean force) {
        try {
            M.enter();
            if (force || !isBooted()) {
                REF.set(new Scannit(configuration));
            } else {
                logger.info("Not re-booting Scannit, because it was already booted.");
            }
            return REF.get();
        } finally {
            M.leave();
        }
    }

    public static boolean isBooted() {
        return REF.get() != null;
    }

    public static void setInstance(Scannit scannit) {
        try {
            M.enter();
            REF.set(scannit);
        } finally {
            M.leave();
        }
    }

    public static Scannit getInstance() {
        try {
            M.enter();
            if (REF.get() != null) {
                return REF.get();
            }
            throw new IllegalStateException("Scannit not set via setInstance(Scannit) or boot(Configuration)");
        } finally {
            M.leave();
        }
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return registryHelper.getTypesAnnotatedWith(annotation, true);
    }

    public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return registryHelper.getMethodsAnnotatedWith(annotation);
    }

    public Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
        return registryHelper.getFieldsAnnotatedWith(annotation);
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> clazz) {
        return registryHelper.getSubTypesOf(clazz);
    }

    private static final Logger logger = LoggerFactory.getLogger(Scannit.class);

}

