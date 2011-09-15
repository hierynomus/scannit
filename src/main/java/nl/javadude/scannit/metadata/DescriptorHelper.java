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

package nl.javadude.scannit.metadata;

import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Helper that converts Java Classes, Fields and Methods to/from a String representation.
 */
public class DescriptorHelper {

    private static List<String> primitiveNames = newArrayList("boolean", "byte", "char", "short", "int", "long", "float", "double");
    private static List<String> primitiveDescriptors = newArrayList("Z", "B", "C", "S", "I", "J", "F", "D");
    @SuppressWarnings({"unchecked"})
    private static List<? extends Class<?>> primitiveClasses = newArrayList(boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class);

    /**
     * Convert the Javassist ClassFile to a String representation.
     * @param file a Javassist ClassFile
     * @return a String representing the full class name
     */
    public static String toTypeDescriptor(ClassFile file) {
        return file.getName();
    }

    /**
     * Convert a Javassist ClassFile and MethodInfo to a String representation.
     * @param file a Javassist ClassFile
     * @param method a Javassist MethodInfo
     * @return a String representing the full class name followed by the method and its signature
     */
    public static String toMethodDescriptor(ClassFile file, MethodInfo method) {
        return new StringBuilder(toTypeDescriptor(file)).append(".").append(method.getName()).append(parameters(method)).toString();
    }

    private static String parameters(MethodInfo method) {
        return Descriptor.toString(method.getDescriptor());
    }

    /**
     * Convert a Javassist ClassFile and FieldInfo to a String representation.
     * @param file a Javassist ClassFile
     * @param field a Javassist FieldInfo
     * @return a String representing the full class name followed by the field name.
     */
    public static String tofieldDescriptor(ClassFile file, FieldInfo field) {
        return new StringBuilder(toTypeDescriptor(file)).append(".").append(field.getName()).toString();
    }

    /**
     * Convert a String representation of the class to a Java class
     * @param descriptor the full class name
     * @return a Java class
     */
    public static Class<?> fromTypeDescriptor(String descriptor) {
        try {
            return Class.forName(descriptor, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found for: " + descriptor, e);
        }
    }

    /**
     * Convert a String representation of the method to a Java Reflect Method
     * @param methodDescriptor the full string representation of the method (including class name and parameters)
     * @return a Java Reflect Method
     */
    public static Method fromMethodDescriptor(String methodDescriptor) {
        int startParameterList = methodDescriptor.indexOf('(');
        int endParameterList = methodDescriptor.indexOf(')');
        String classAndMethodName = methodDescriptor.substring(0, startParameterList);
        int classMethodSep = classAndMethodName.lastIndexOf('.');
        Class<?> aClass = fromTypeDescriptor(classAndMethodName.substring(0, classMethodSep));
        String methodName = classAndMethodName.substring(classMethodSep + 1, startParameterList);
        Class<?>[] parameters = parameters(methodDescriptor.substring(startParameterList + 1, endParameterList));
        try {
            return aClass.getMethod(methodName, parameters);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method " + methodName + " with parameters: " + Arrays.toString(parameters) + " could not be located.", e);
        }
    }

    private static Class<?>[] parameters(String parameters) {
        if (parameters == null || parameters.isEmpty()) return new Class[0];

        String[] paramDescriptors = parameters.split(", *");
        Class<?>[] clazzes = new Class<?>[paramDescriptors.length];
        for (int i = 0; i < paramDescriptors.length; i++) {
            String paramDescriptor = paramDescriptors[i];
            if (isPrimitive(paramDescriptor)) {
                clazzes[i] = primitiveClasses.get(primitiveNames.indexOf(paramDescriptor));
            } else {
                String type = paramDescriptor;
                if (isArray(paramDescriptor)) {
                    String arrayType = paramDescriptor.substring(0, paramDescriptor.indexOf('['));
                    if (isPrimitive(arrayType)) {
                        type = "[" + primitiveDescriptors.get(primitiveNames.indexOf(arrayType));
                    } else {
                        type = "[L" + arrayType + ";";
                    }
                }
                try {
                    clazzes[i] = Class.forName(type, false, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Could not load class: " + paramDescriptor, e);
                }
            }
        }
        return clazzes;
    }

    private static boolean isArray(String paramDescriptor) {
        return paramDescriptor.indexOf('[') != -1;
    }

    private static boolean isPrimitive(String paramDescriptor) {
        return primitiveNames.contains(paramDescriptor);
    }

    /**
     * Convert a string representation of a field to a Java Reflect Field.
     * @param fieldDescriptor a String representation of a Field (including the class name)
     * @return a Java Reflect Field
     */
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
}

