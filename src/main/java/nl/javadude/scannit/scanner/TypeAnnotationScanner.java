package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.Registry;

import java.util.List;

import static nl.javadude.scannit.metadata.DescriptorHelper.toTypeDescriptor;
import static nl.javadude.scannit.metadata.JavassistHelper.getTypeAnnotations;

public class TypeAnnotationScanner extends AbstractScanner {
    public void doScan(ClassFile file, Registry registry) {
        List<String> typeAnnotations = getTypeAnnotations(file);

        for (String typeAnnotation : typeAnnotations) {
            registry.get(this).put(typeAnnotation, toTypeDescriptor(file));
        }
    }
}
