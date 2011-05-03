package nl.javadude.scannit.metadata;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import nl.javadude.scannit.ObjectUnderTest;
import nl.javadude.scannit.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class DescriptorHelperTest {

    private ClassFile classFile;
    private String fullTestName;

    @Before
    public void init() throws IOException {
        classFile = TestUtils.readClass(ObjectUnderTest.FullTestClass.class);
        fullTestName = ObjectUnderTest.FullTestClass.class.getName();
    }

    @Test
    public void shouldConvertClassFileToDescriptor() {
        assertThat(DescriptorHelper.toTypeDescriptor(classFile), equalTo(fullTestName));
    }
    
    @Test
    public void shouldConvertFieldToDescriptor() {
        List<FieldInfo> fields = classFile.getFields();
        FieldInfo foo = Collections2.filter(fields, new Predicate<FieldInfo>() {
            public boolean apply(FieldInfo input) {
                return input.getName().equals("foo");
            }
        }).iterator().next();

        assertThat(DescriptorHelper.tofieldDescriptor(classFile, foo), equalTo(fullTestName + ".foo"));
    }
}
