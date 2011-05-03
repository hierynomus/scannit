package nl.javadude.scannit;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import nl.javadude.scannit.scanner.Scanner;

import java.util.*;

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

    public Multimap<String, String> get(Scanner scanner) {
        return register.get(scanner.getClass().getSimpleName());
    }
}
