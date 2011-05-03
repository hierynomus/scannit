package nl.javadude.scannit.metadata;

import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Field;

public class DescriptorHelper {

    public static String toTypeDescriptor(ClassFile file) {
        return file.getName();
    }

    public static Class<?> fromTypeDescriptor(String descriptor) {
        try {
            return Class.forName(descriptor);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found for: " + descriptor, e);
        }
    }

    public static String toMethodDescriptor(ClassFile file, MethodInfo method) {
        return new StringBuilder(toTypeDescriptor(file)).append(".").append(method.getName()).append(parameters(method)).toString();
    }

    public static String tofieldDescriptor(ClassFile file, FieldInfo field) {
        return new StringBuilder(toTypeDescriptor(file)).append(".").append(field.getName()).toString();
    }

    public static Field fromFieldDescriptor(String fieldDescriptor) {
        int lastDot = fieldDescriptor.lastIndexOf('.');
        String className = fieldDescriptor.substring(0, lastDot);
        String fieldName = fieldDescriptor.substring(lastDot + 1);
        Class<?> aClass = fromTypeDescriptor(className);
        try {
            return aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " not found on class " + className, e);
        }
    }

    private static String parameters(MethodInfo method) {
        return Descriptor.toString(method.getDescriptor());
    }
}
