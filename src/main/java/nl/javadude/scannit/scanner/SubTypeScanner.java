package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.Registry;
import nl.javadude.scannit.filter.Filter;

import static nl.javadude.scannit.metadata.DescriptorHelper.toTypeDescriptor;

public class SubTypeScanner extends AbstractScanner {

    public SubTypeScanner() {
        addFilter(Filter.exclude(Object.class.getName()));
    }

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
