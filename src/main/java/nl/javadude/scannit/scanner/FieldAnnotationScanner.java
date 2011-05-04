package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import nl.javadude.scannit.registry.Registry;

import java.util.List;

import static nl.javadude.scannit.metadata.DescriptorHelper.tofieldDescriptor;
import static nl.javadude.scannit.metadata.JavassistHelper.getFieldAnnotations;

/**
 * A Scanner implementation which scans for annotations on Fields.
 */
public class FieldAnnotationScanner extends AbstractScanner {

    /**
     * Scan for annotations on fields.
     * @param file The ClassFile to be scanned
     * @param registry The registry to store the scanned information in.
     */
    public void doScan(ClassFile file, Registry registry) {
        List<FieldInfo> fields = file.getFields();
        for (FieldInfo field : fields) {
            List<String> fieldAnnotations = getFieldAnnotations(field);
            registerAnnotations(fieldAnnotations, file, field, registry);
        }
    }

    private void registerAnnotations(List<String> fieldAnnotations, ClassFile file, FieldInfo field, Registry registry) {
        for (String fieldAnnotation : fieldAnnotations) {
            registry.get(this).put(fieldAnnotation, tofieldDescriptor(file, field));
        }
    }
}
