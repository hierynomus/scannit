package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.Registry;

public interface Scanner {
    void scan(ClassFile file, Registry registry);
}
