package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.Registry;
import nl.javadude.scannit.filter.Filter;

import static nl.javadude.scannit.filter.Filter.chain;
import static nl.javadude.scannit.filter.Filter.include;

public abstract class AbstractScanner {

    // By default include everything.
    private Filter filter = include(".*");

    public void addFilter(Filter filter) {
        this.filter = chain(filter, this.filter);
    }

    public boolean accepts(String name) {
        return filter.apply(name);
    }

    public void scan(ClassFile file, Registry registry) {
        if (accepts(file.getName())) {
            doScan(file, registry);
        }
    }

    protected abstract void doScan(ClassFile file, Registry registry);
}
