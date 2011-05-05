/* License added by: GRADLE-LICENSE-PLUGIN
 *
 *    Copyright 2011 Jeroen van Erp (jeroen@javadude.nl)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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

