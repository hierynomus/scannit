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
