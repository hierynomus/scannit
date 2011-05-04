package nl.javadude.scannit.scanner;

import nl.javadude.scannit.registry.Registry;
import org.junit.Before;

public abstract class AbstractScannerTest {
    protected Registry registry;
    protected AbstractScanner scanner;

    @Before
    public void init() {
        registry = new Registry();
        scanner = getScanner();
    }

    protected abstract AbstractScanner getScanner();


    
}
