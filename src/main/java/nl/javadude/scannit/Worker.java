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

import de.schlichtherle.truezip.file.TFile;
import javassist.bytecode.ClassFile;
import nl.javadude.scannit.metadata.JavassistHelper;
import nl.javadude.scannit.reader.ClasspathReader;
import nl.javadude.scannit.reader.URIReader;
import nl.javadude.scannit.registry.Registry;
import nl.javadude.scannit.scanner.AbstractScanner;

import java.net.URI;
import java.util.List;
import java.util.Set;

class Worker {

    private Configuration configuration;
    private Registry registry;

    Worker(Configuration configuration, Registry registry) {
        this.configuration = configuration;
        this.registry = registry;
    }

    void scan() {
        ClasspathReader reader = new ClasspathReader();
        for (String prefix : configuration.prefixes) {
            Set<URI> baseURIs = reader.findBaseURIs(prefix);
            scanURI(baseURIs, prefix);
        }
    }

    private void scanURI(Set<URI> baseURIs, String prefix) {
        URIReader uriReader = new URIReader();
        for (URI baseURI : baseURIs) {
            List<TFile> tFiles = uriReader.listFiles(baseURI);
            scanFiles(tFiles, prefix);
        }
    }

    private void scanFiles(List<TFile> tFiles, String prefix) {
        for (TFile tFile : tFiles) {
            if (tFile.getName().endsWith(".class")) {
                ClassFile classFile = JavassistHelper.readFile(tFile);
                scanFile(classFile);
            }
        }
    }

    private void scanFile(ClassFile classFile) {
        for (AbstractScanner scanner : configuration.scanners) {
            scanner.scan(classFile, registry);
        }
    }


}

