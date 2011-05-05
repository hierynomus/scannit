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

package nl.javadude.scannit.scanner;

import nl.javadude.scannit.registry.Registry;
import org.junit.Before;

public abstract class AbstractScannerTest {
    protected Registry registry;
    protected AbstractScanner scanner;

    @Before
    public void init() {
        registry = new Registry();
        scanner = getScanner();
    }

    protected abstract AbstractScanner getScanner();


    
}

