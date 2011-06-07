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

package nl.javadude.scannit;

import nl.javadude.scannit.filter.Filter;
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
        for (AbstractScanner scanner : scanners) {
            for (String prefix : prefixes) {
                scanner.addFilter(toFilter(prefix));
            }
            this.scanners.add(scanner);
        }
        return this;
    }

    /**
     * Add a prefix to scan to the configuration
     * @param prefix the package prefix to scan
     * @return this
     */
    public Configuration scan(String prefix) {
        prefixes.add(prefix);
        for (AbstractScanner scanner : scanners) {
            scanner.addFilter(toFilter(prefix));
        }
        return this;
    }

    private Filter toFilter(String prefix) {
        return Filter.include(prefix.replace(".", "\\.") + ".*");
    }
}

