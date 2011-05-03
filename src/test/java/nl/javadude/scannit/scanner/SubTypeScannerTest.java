package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.ObjectUnderTest;
import nl.javadude.scannit.TestUtils;
import org.junit.Test;

import java.io.IOException;

import static nl.javadude.scannit.TestUtils.readClass;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class SubTypeScannerTest extends AbstractScannerTest {

    @Override
    protected AbstractScanner getScanner() {
        return new SubTypeScanner();
    }
    
    @Test
    public void shouldScanSubTypes() throws IOException {
        Class<ObjectUnderTest.Child> aClass = ObjectUnderTest.Child.class;
        ClassFile classFile = readClass(aClass);
        scanner.scan(classFile, registry);

        assertThat(registry.get(scanner).get(ObjectUnderTest.Parent.class.getName()), hasItem(aClass.getName()));
    }

    @Test
    public void shouldScanInterfaces() throws IOException {
        Class<ObjectUnderTest.Parent> aClass = ObjectUnderTest.Parent.class;
        ClassFile classFile = readClass(aClass);
        scanner.scan(classFile, registry);
        assertThat(registry.get(scanner).get(ObjectUnderTest.Person.class.getName()), hasItem(aClass.getName()));
    }
}
