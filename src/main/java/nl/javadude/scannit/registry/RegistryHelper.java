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

package nl.javadude.scannit.registry;

import nl.javadude.scannit.scanner.FieldAnnotationScanner;
import nl.javadude.scannit.scanner.MethodAnnotationScanner;
import nl.javadude.scannit.scanner.SubTypeScanner;
import nl.javadude.scannit.scanner.TypeAnnotationScanner;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singleton;
import static nl.javadude.scannit.metadata.DescriptorHelper.*;

public class RegistryHelper {
    private Registry registry;

    public RegistryHelper(Registry registry) {
        this.registry = registry;
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation, boolean findInheritors) {
        Set<String> strings = new HashSet<String>(registry.get(TypeAnnotationScanner.class).get(annotation.getName()));
        if (findInheritors && annotation.isAnnotationPresent(Inherited.class)) {
            Set<String> moreStrings = new HashSet<String>();
            findSubtypes(strings, moreStrings);
            strings.addAll(moreStrings);
        }

        return convertToClassSet(strings);
    }

    private <T> Set<Class<? extends T>> convertToClassSet(Set<String> strings) {
        Set<Class<? extends T>> clazzes = new HashSet<Class<? extends T>>();
        for (String string : strings) {
            clazzes.add((Class<? extends T>) fromTypeDescriptor(string));
        }

        return Collections.unmodifiableSet(clazzes);
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> clazz) {
        HashSet<String> strings = new HashSet<String>();
        findSubtypes(singleton(clazz.getName()), strings);
        return this.<T>convertToClassSet(strings);
    }

    private void findSubtypes(Collection<String> superclazzes, Set<String> subclazzesCollector) {
        for (String superclazz : superclazzes) {
            Collection<String> strings = registry.get(SubTypeScanner.class).get(superclazz);
            if (strings != null && !strings.isEmpty()) {
                subclazzesCollector.addAll(strings);
                findSubtypes(strings, subclazzesCollector);
            }
        }

    }

    public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        Collection<String> strings = registry.get(MethodAnnotationScanner.class).get(annotation.getName());
        Set<Method> methods = new HashSet<Method>();
        if (strings != null) {
            for (String string : strings) {
                methods.add(fromMethodDescriptor(string));
            }
        }
        return methods;
    }

    public Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
        Collection<String> strings = registry.get(FieldAnnotationScanner.class).get(annotation.getName());
        Set<Field> fields = new HashSet<Field>();
        if (strings != null) {
            for (String string : strings) {
                fields.add(fromFieldDescriptor(string));
            }
        }
        return fields;
    }
}

