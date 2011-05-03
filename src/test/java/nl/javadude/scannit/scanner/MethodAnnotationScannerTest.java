package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.ObjectUnderTest;
import org.junit.Test;

import java.io.IOException;

import static nl.javadude.scannit.TestUtils.readClass;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class MethodAnnotationScannerTest extends AbstractScannerTest {
    @Override
    protected Scanner getScanner() {
        return new MethodAnnotationScanner();
    }

    @Test
    public void shouldScanAnnotations() throws IOException {
        ClassFile classFile = readClass(ObjectUnderTest.TestClassWithAnnotatedMethod.class);
        scanner.scan(classFile, registry);
        assertThat(registry.get(scanner), notNullValue());
        assertThat(registry.get(scanner).size(), equalTo(1));
        assertThat(registry.get(scanner).get(ObjectUnderTest.MethodAnnotation.class.getName()), hasItem(ObjectUnderTest.TestClass.class.getName()));
    }
}
