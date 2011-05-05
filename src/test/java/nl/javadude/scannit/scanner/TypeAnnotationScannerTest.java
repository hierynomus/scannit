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
import nl.javadude.scannit.ObjectUnderTest;
import org.junit.Test;

import java.io.IOException;

import static nl.javadude.scannit.TestUtils.readClass;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class TypeAnnotationScannerTest extends AbstractScannerTest {
    @Override
    protected AbstractScanner getScanner() {
        return new TypeAnnotationScanner();
    }

    @Test
    public void shouldFindAnnotations() throws IOException {
        ClassFile classFile = readClass(ObjectUnderTest.AnnotatedTestClass.class);

        scanner.scan(classFile, registry);
        assertThat(registry.get(scanner), notNullValue());
        assertThat(registry.get(scanner).size(), equalTo(1));
        assertThat(registry.get(scanner).get(ObjectUnderTest.ClassAnnotation.class.getName()), hasItem(ObjectUnderTest.AnnotatedTestClass.class.getName()));
    }


}

