package nl.javadude.scannit;

import nl.javadude.scannit.registry.Registry;
import nl.javadude.scannit.registry.RegistryHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class Scannit {
    final Registry registry = new Registry();
    private RegistryHelper registryHelper;

    public Scannit(Configuration configuration) {
        new Worker(configuration, registry).scan();
        registryHelper = new RegistryHelper(registry);
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
