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

import com.google.common.base.Predicate;
import de.schlichtherle.truezip.file.TFile;
import javassist.bytecode.ClassFile;
import nl.javadude.scannit.metadata.JavassistHelper;
import nl.javadude.scannit.reader.ArchiveEntrySupplier;
import nl.javadude.scannit.reader.ClasspathReader;
import nl.javadude.scannit.registry.Registry;
import nl.javadude.scannit.scanner.AbstractScanner;

import java.net.URI;
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
            scanURI(baseURIs);
        }
    }

    private void scanURI(Set<URI> baseURIs) {
        for (URI baseURI : baseURIs) {
	        ArchiveEntrySupplier entrySupplier = new ArchiveEntrySupplier(baseURI);
	        scanFiles(entrySupplier);
        }
    }


    private void scanFiles(ArchiveEntrySupplier entrySupplier) {
	    entrySupplier.withArchiveEntries(new Predicate<TFile>() {
		    public boolean apply(TFile tFile) {
			    if (tFile.getName().endsWith(".class")) {
			        ClassFile classFile = JavassistHelper.readFile(tFile);
			        scanFile(classFile);
			    }
			    return true;
		    }
	    });
    }

    private void scanFile(ClassFile classFile) {
        for (AbstractScanner scanner : configuration.scanners) {
            scanner.scan(classFile, registry);
        }
    }


}

