package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import nl.javadude.scannit.registry.Registry;

import java.util.List;

import static nl.javadude.scannit.metadata.DescriptorHelper.toMethodDescriptor;
import static nl.javadude.scannit.metadata.JavassistHelper.getMethodAnnotations;

/**
 * A scanner that scans for annotations on methods.
 */
public class MethodAnnotationScanner extends AbstractScanner {
    /**
     * Scan for annotations on methods
     * @param file The ClassFile to be scanned
     * @param registry The registry to store the scanned information in.
     */
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
