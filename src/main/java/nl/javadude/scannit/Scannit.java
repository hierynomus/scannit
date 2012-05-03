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

import nl.javadude.scannit.registry.Registry;
import nl.javadude.scannit.registry.RegistryHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class Scannit {
	private static final AtomicReference<Scannit> REF = new AtomicReference<Scannit>();

    final Registry registry = new Registry();
    private RegistryHelper registryHelper;

    public Scannit(Configuration configuration) {
	    configuration.wireScanners();
        new Worker(configuration, registry).scan();
        registryHelper = new RegistryHelper(registry);
    }

	public static synchronized Scannit boot(Configuration configuration) {
		REF.set(new Scannit(configuration));
		return REF.get();
	}

	public static synchronized void setInstance(Scannit scannit) {
		REF.set(scannit);
	}

	public static synchronized Scannit getInstance() {
		if (REF.get() != null) {
			return REF.get();
		}
		throw new IllegalStateException("Scannit not set via setInstance(Scannit) or boot(Configuration)");
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
}

