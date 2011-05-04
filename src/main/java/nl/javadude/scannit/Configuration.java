package nl.javadude.scannit;

import nl.javadude.scannit.scanner.AbstractScanner;

import java.util.Arrays;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * A Scannit Configuration object.
 */
public class Configuration {
    Set<AbstractScanner> scanners = newHashSet();
    Set<String> prefixes = newHashSet();

    /**
     * private constructor, use the factory
     */
    private Configuration() {
    }

    /**
     * Factory method for a configuration.
     * @return a new Configuration
     */
    public static Configuration config() {
        return new Configuration();
    }

    /**
     * Add one or more scanners to this configuration.
     * @param scanners the scanners to add.
     * @return this
     */
    public Configuration with(AbstractScanner... scanners) {
        this.scanners.addAll(Arrays.asList(scanners));
        return this;
    }

    /**
     * Add a prefix to scan to the configuration
     * @param prefix the package prefix to scan
     * @return this
     */
    public Configuration scan(String prefix) {
        prefixes.add(prefix);
        return this;
    }
}
