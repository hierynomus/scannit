package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.registry.Registry;

import java.util.List;

import static nl.javadude.scannit.metadata.DescriptorHelper.toTypeDescriptor;
import static nl.javadude.scannit.metadata.JavassistHelper.getTypeAnnotations;

/**
 * Scans for annotations on the class level.
 */
public class TypeAnnotationScanner extends AbstractScanner {
    /**
     * Scan the ClassFile for annotations
     * @param file The ClassFile to be scanned
     * @param registry The registry to store the scanned information in.
     */
    public void doScan(ClassFile file, Registry registry) {
        List<String> typeAnnotations = getTypeAnnotations(file);

        for (String typeAnnotation : typeAnnotations) {
            registry.get(this).put(typeAnnotation, toTypeDescriptor(file));
        }
    }
}
