package nl.javadude.scannit.registry;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import nl.javadude.scannit.scanner.*;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * The registry of everything that Scannit scanned.
 */
public class Registry {
    private final Map<String, Multimap<String, String>> register = new MapMaker().makeComputingMap(new Function<String, Multimap<String, String>>() {
        public Multimap<String, String> apply(String input) {
            return Multimaps.newSetMultimap(new HashMap<String, Collection<String>>(), new Supplier<Set<String>>() {
                public Set<String> get() {
                    return Sets.newHashSet();
                }
            });
        }
    });

    /**
     * Get the registry of the scanner
     * @param scanner the scanner that needs to store or retrieve information
     * @return the scanned information of this scanner
     */
    public Multimap<String, String> get(AbstractScanner scanner) {
        return get(scanner.getClass());
    }

    /**
     * Get the registry of the scanner
     * @param clazz the scanner clazz for which we want to retrieve information
     * @return the scanned information of this scanner
     */
    public Multimap<String, String> get(Class<? extends AbstractScanner> clazz) {
        return register.get(clazz.getName());
    }
}
