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

import com.google.common.base.Predicates;
import javassist.bytecode.ClassFile;
import nl.javadude.scannit.registry.Registry;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static nl.javadude.scannit.metadata.DescriptorHelper.toTypeDescriptor;

/**
 * A scanner that registers the type hierarchy for a class.
 */
public class SubTypeScanner extends AbstractScanner {

    /**
     * Constructor.
     * Excludes the java.lang.Object class from the supertypes list (as everything is an object).
     */
    public SubTypeScanner() {
        addFilter(Predicates.<CharSequence>not(Predicates.<CharSequence>equalTo(Object.class.getName())));
    }

    /**
     * Scans the ClassFile and builds up the type hierarchy (including the annotations).
     * @param file The ClassFile to be scanned
     * @param registry The registry to store the scanned information in.
     */
    @Override
    protected void doScan(ClassFile file, Registry registry) {
        String superclass = file.getSuperclass();
        if (accepts(superclass)) {
            registry.get(this).put(superclass, toTypeDescriptor(file));
        }

        for (String intf : file.getInterfaces()) {
            if (accepts(intf)) {
                registry.get(this).put(intf, toTypeDescriptor(file));
            }
        }
    }
}

