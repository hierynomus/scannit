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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import nl.javadude.scannit.metadata.DescriptorHelper;
import nl.javadude.scannit.scanner.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.newHashSet;

public class RegistryHelper {
    private Registry registry;

    public RegistryHelper(Registry registry) {
        this.registry = registry;
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation,  boolean findInheritors) {
        Set<String> strings = newHashSet(registry.get(TypeAnnotationScanner.class).get(annotation.getName()));

        if (findInheritors && annotation.isAnnotationPresent(Inherited.class)) {
            strings.addAll(findSubtypes(strings));
        }

        return convertToClassSet(strings);
    }

    private <T> ImmutableSet<Class<? extends T>> convertToClassSet(Set<String> strings) {
        return copyOf(transform(strings, new Function<String, Class<? extends T>>() {
            public Class<? extends T> apply(String input) {
                return (Class<? extends T>) DescriptorHelper.fromTypeDescriptor(input);
            }
        }));
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> clazz) {
        HashSet<String> strings = newHashSet(registry.get(SubTypeScanner.class).get(clazz.getName()));
        strings.addAll(findSubtypes(strings));
        return this.<T>convertToClassSet(strings);
    }

    private Collection<String> findSubtypes(Collection<String> superclazzes) {
        Set<String> subclazzes = newHashSet();
        for (String superclazz : superclazzes) {
            Collection<String> strings = registry.get(SubTypeScanner.class).get(superclazz);
            if (!strings.isEmpty()) {
                subclazzes.addAll(strings);
                subclazzes.addAll(findSubtypes(subclazzes));
            }
        }

        return subclazzes;
    }

    public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return getAnnotatedThings(annotation, MethodAnnotationScanner.class, new Function<String, Method>() {
            public Method apply(String input) {
                return DescriptorHelper.fromMethodDescriptor(input);
            }
        });
    }

    public Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
        return getAnnotatedThings(annotation, FieldAnnotationScanner.class, new Function<String, Field>() {
            public Field apply(String input) {
                return DescriptorHelper.fromFieldDescriptor(input);
            }
        });
    }

    private <T> Set<T> getAnnotatedThings(Class<? extends Annotation> annotation, Class<? extends AbstractScanner> clazz, Function<String, T> function) {
        Collection<String> strings = registry.get(clazz).get(annotation.getName());
        return copyOf(transform(strings, function));
    }
}

