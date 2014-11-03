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

import nl.javadude.scannit.scanner.AbstractScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The registry of everything that Scannit scanned.
 */
public class Registry {

    private final Map<String, Map<String, Set<String>>> register = new HashMap<String, Map<String, Set<String>>>();

    /**
     * Get the registry of the scanner
     *
     * @param scanner the scanner that needs to store or retrieve information
     * @return the scanned information of this scanner
     */
    public Map<String, Set<String>> get(AbstractScanner scanner) {
        return get(scanner.getClass());
    }

    /**
     * Get the registry of the scanner
     *
     * @param clazz the scanner clazz for which we want to retrieve information
     * @return the scanned information of this scanner
     */
    public Map<String, Set<String>> get(Class<? extends AbstractScanner> clazz) {
        String name = clazz.getName();
        if (!register.containsKey(name)) {
            register.put(name, new HashMap<String, Set<String>>());
        }
        return register.get(name);
    }
}

