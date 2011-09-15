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

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import nl.javadude.scannit.registry.Registry;

import java.util.List;

import static nl.javadude.scannit.metadata.DescriptorHelper.tofieldDescriptor;
import static nl.javadude.scannit.metadata.JavassistHelper.getFieldAnnotations;

/**
 * A Scanner implementation which scans for annotations on Fields.
 */
public class FieldAnnotationScanner extends AbstractScanner {

    /**
     * Scan for annotations on fields.
     * @param file The ClassFile to be scanned
     * @param registry The registry to store the scanned information in.
     */
    public void doScan(ClassFile file, Registry registry) {
        @SuppressWarnings({"unchecked"}) List<FieldInfo> fields = file.getFields();
        for (FieldInfo field : fields) {
            List<String> fieldAnnotations = getFieldAnnotations(field);
            registerAnnotations(fieldAnnotations, file, field, registry);
        }
    }

    private void registerAnnotations(List<String> fieldAnnotations, ClassFile file, FieldInfo field, Registry registry) {
        for (String fieldAnnotation : fieldAnnotations) {
            registry.get(this).put(fieldAnnotation, tofieldDescriptor(file, field));
        }
    }
}

