package nl.javadude.scannit.metadata;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;

public class DescriptorHelper {

    public static String toTypeDescriptor(ClassFile file) {
        return file.getName();
    }

    public static String toMethodDescriptor(ClassFile file, MethodInfo method) {
        return new StringBuilder(toTypeDescriptor(file)).append(".").append(method.getName()).append(parameters(method)).toString();
    }

    public static String tofieldDescriptor(ClassFile file, FieldInfo field) {
        return new StringBuilder(toTypeDescriptor(file)).append(".").append(field.getName()).toString();
    }

    private static String parameters(MethodInfo method) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
