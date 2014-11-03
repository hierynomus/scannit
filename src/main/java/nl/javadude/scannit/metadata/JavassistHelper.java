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

package nl.javadude.scannit.metadata;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavassistHelper {

    private static final Logger logger = LoggerFactory.getLogger(JavassistHelper.class);

    public static ClassFile readFile(TFile file) {
        try {
            TFileInputStream tFileInputStream = new TFileInputStream(file);
            return readFromInputStream(tFileInputStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find file: " + file, e);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read file: " + file, e);
        }
    }

    public static ClassFile readFromInputStream(InputStream is) throws IOException {
        if (is == null) throw new IllegalArgumentException("Cannot read from a null inputstream");
        DataInputStream in = null;
        try {
            in = new DataInputStream(new BufferedInputStream(is));
            return new ClassFile(in);
        } finally {
            closeQuietly(in);
        }
    }

    public static List<String> getTypeAnnotations(ClassFile file) {
        AnnotationsAttribute attribute = (AnnotationsAttribute) file.getAttribute(AnnotationsAttribute.visibleTag);
        return readAnnotations(attribute);
    }

    private static List<String> readAnnotations(AnnotationsAttribute attribute) {
        if (attribute == null) return new ArrayList<String>();
        Annotation[] annotations = attribute.getAnnotations();
        List<String> anns = new ArrayList<String>();
        for (Annotation annotation : annotations) {
            anns.add(annotation.getTypeName());
        }
        return anns;
    }

    public static List<String> getMethodAnnotations(MethodInfo method) {
        AnnotationsAttribute attribute = (AnnotationsAttribute) method.getAttribute(AnnotationsAttribute.visibleTag);
        return readAnnotations(attribute);
    }

    public static List<String> getFieldAnnotations(FieldInfo field) {
        AnnotationsAttribute attribute = (AnnotationsAttribute) field.getAttribute(AnnotationsAttribute.visibleTag);
        return readAnnotations(attribute);
    }

    public static void closeQuietly(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            logger.warn("IOException when closing Closeable", e);
        }
    }
}

