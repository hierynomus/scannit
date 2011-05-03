package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.ObjectUnderTest;
import nl.javadude.scannit.Registry;
import nl.javadude.scannit.metadata.JavassistHelper;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractScannerTest {
    protected Registry registry;
    protected Scanner scanner;

    @Before
    public void init() {
        registry = new Registry();
        scanner = getScanner();
    }

    protected abstract Scanner getScanner();


    
}
