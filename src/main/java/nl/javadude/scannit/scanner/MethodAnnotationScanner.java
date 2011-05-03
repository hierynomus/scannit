package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import nl.javadude.scannit.Registry;

import java.util.List;

import static nl.javadude.scannit.metadata.DescriptorHelper.toMethodDescriptor;
import static nl.javadude.scannit.metadata.JavassistHelper.getMethodAnnotations;

public class MethodAnnotationScanner extends AbstractScanner {
    public void doScan(ClassFile file, Registry registry) {
        List<MethodInfo> methods = file.getMethods();
        for (MethodInfo method : methods) {
            List<String> methodAnnotations = getMethodAnnotations(method);
            registerAnnotations(methodAnnotations, file, method, registry);
        }
    }

    private void registerAnnotations(List<String> methodAnnotations, ClassFile file, MethodInfo method, Registry registry) {
        for (String methodAnnotation : methodAnnotations) {
            registry.get(this).put(methodAnnotation, toMethodDescriptor(file, method));
        }
    }
}
