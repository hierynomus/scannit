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

public class TypeAnnotationScannerTest extends AbstractScannerTest {
    @Override
    protected AbstractScanner getScanner() {
        return new TypeAnnotationScanner();
    }

    @Test
    public void shouldFindAnnotations() throws IOException {
        ClassFile classFile = readClass(ObjectUnderTest.AnnotatedTestClass.class);

        scanner.scan(classFile, registry);
        assertThat(registry.get(scanner), notNullValue());
        assertThat(registry.get(scanner).size(), equalTo(1));
        assertThat(registry.get(scanner).get(ObjectUnderTest.ClassAnnotation.class.getName()), hasItem(ObjectUnderTest.AnnotatedTestClass.class.getName()));
    }


}
