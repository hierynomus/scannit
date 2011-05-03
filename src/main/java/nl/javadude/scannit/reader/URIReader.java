package nl.javadude.scannit.reader;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import javassist.bytecode.ClassFile;
import nl.javadude.scannit.metadata.JavassistHelper;

import java.io.FileNotFoundException;
import java.net.URI;

public class URIReader {

    public void readUri(URI uri) {
        TFile tFile = new TFile(uri);
        listDirectory(tFile);
    }

    private void listDirectory(TFile tFile) {
        for (TFile file : tFile.listFiles()) {
            if (file.isArchive() || file.isDirectory()) {
                listDirectory(file);
            } else if (file.isEntry()) {
                ClassFile classFile = JavassistHelper.readFile(file);

            }
        }
    }
}
