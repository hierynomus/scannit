package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.registry.Registry;
import nl.javadude.scannit.filter.Filter;

import static nl.javadude.scannit.filter.Filter.chain;
import static nl.javadude.scannit.filter.Filter.include;

/**
 * An abstract scanner implementation which does filtering of the inputs.
 */
public abstract class AbstractScanner {
    private static final Filter INCLUDE_ALL = include(".*");

    // By default include everything.
    private Filter filter = INCLUDE_ALL;

    /**
     * Chain a new Filter to the curent one.
     * @param filter
     */
    public void addFilter(Filter filter) {
        this.filter = chain(filter, this.filter);
    }

    /**
     * Reset the Filter to include everything
     */
    public void clearFilter() {
        this.filter = INCLUDE_ALL;
    }

    /**
     * Determine whether this scanner accepts the name of this entry.
     * @param name the name of an entry
     * @return true if the filters accept the name
     */
    public boolean accepts(String name) {
        return filter.apply(name);
    }

    /**
     * Scan a ClassFile (if it is accepted)
     * @param file The Javassist ClassFile to scan.
     * @param registry The Registry to store the scanned information in.
     */
    public void scan(ClassFile file, Registry registry) {
        if (accepts(file.getName())) {
            doScan(file, registry);
        }
    }

    /**
     * Overridden by subclasses to do the actual scanning.
     * @param file The ClassFile to be scanned
     * @param registry The registry to store the scanned information in.
     */
    protected abstract void doScan(ClassFile file, Registry registry);
}
