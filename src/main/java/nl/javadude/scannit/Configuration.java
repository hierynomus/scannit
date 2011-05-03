package nl.javadude.scannit;

import nl.javadude.scannit.scanner.AbstractScanner;

import java.util.Arrays;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class Configuration {
    Set<AbstractScanner> scanners = newHashSet();
    Set<String> prefixes = newHashSet();

    private Configuration() {
    }

    public static Configuration config() {
        return new Configuration();
    }

    public Configuration with(AbstractScanner... scanners) {
        this.scanners.addAll(Arrays.asList(scanners));
        return this;
    }

    public Configuration scan(String prefix) {
        prefixes.add(prefix);
        return this;
    }
}
