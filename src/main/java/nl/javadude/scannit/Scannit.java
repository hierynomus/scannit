package nl.javadude.scannit;

import de.schlichtherle.truezip.file.TFile;
import nl.javadude.scannit.reader.ClasspathReader;

import java.net.URI;
import java.util.Set;

public class Scannit {

    public static Scannit scan(String packagePrefix) {
        Set<URI> baseURIs = new ClasspathReader().findBaseURIs(packagePrefix);
        for (URI baseURI : baseURIs) {
            TFile file = new TFile(baseURI);
        }
        return null;
    }
}
