package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.registry.Registry;
import nl.javadude.scannit.filter.Filter;

import static nl.javadude.scannit.metadata.DescriptorHelper.toTypeDescriptor;

/**
 * A scanner that registers the type hierarchy for a class.
 */
public class SubTypeScanner extends AbstractScanner {

    /**
     * Constructor.
     * Excludes the java.lang.Object class from the supertypes list (as everything is an object).
     */
    public SubTypeScanner() {
        addFilter(Filter.exclude(Object.class.getName()));
    }

    /**
     * Scans the ClassFile and builds up the type hierarchy (including the annotations).
     * @param file The ClassFile to be scanned
     * @param registry The registry to store the scanned information in.
     */
    @Override
    protected void doScan(ClassFile file, Registry registry) {
        String superclass = file.getSuperclass();
        if (accepts(superclass)) {
            registry.get(this).put(superclass, toTypeDescriptor(file));
        }

        for (String intf : file.getInterfaces()) {
            if (accepts(intf)) {
                registry.get(this).put(intf, toTypeDescriptor(file));
            }
        }
    }
}
