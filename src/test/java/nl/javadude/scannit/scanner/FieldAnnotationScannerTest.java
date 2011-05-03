package nl.javadude.scannit.scanner;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.ObjectUnderTest;
import nl.javadude.scannit.TestUtils;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class FieldAnnotationScannerTest extends AbstractScannerTest {

    @Override
    protected AbstractScanner getScanner() {
        return new FieldAnnotationScanner();
    }

    @Test
    public void shouldScanAnnotations() throws IOException {
        ClassFile classFile = TestUtils.readClass(ObjectUnderTest.TestClassWithAnnotatedField.class);
        scanner.scan(classFile, registry);
        assertThat(registry.get(scanner), notNullValue());
        assertThat(registry.get(scanner).size(), equalTo(1));
        String className = ObjectUnderTest.TestClassWithAnnotatedField.class.getName();
        assertThat(registry.get(scanner).get(ObjectUnderTest.FieldAnnotation.class.getName()), hasItem(className + ".foo"));
    }
}
