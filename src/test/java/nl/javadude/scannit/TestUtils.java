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

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.metadata.JavassistHelper;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    public static InputStream getStreamFromClass(Class<?> aClass) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(aClass.getName().replaceAll("\\.", "/") + ".class");
    }

    public static ClassFile readClass(Class<?> aClass) throws IOException {
        InputStream resourceAsStream = getStreamFromClass(aClass);
        return JavassistHelper.readFromInputStream(resourceAsStream);
    }


}

