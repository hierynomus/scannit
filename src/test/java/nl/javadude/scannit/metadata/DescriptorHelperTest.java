package nl.javadude.scannit.metadata;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import nl.javadude.scannit.ObjectUnderTest;
import nl.javadude.scannit.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static nl.javadude.scannit.TestUtils.readClass;
import static nl.javadude.scannit.metadata.DescriptorHelper.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DescriptorHelperTest {

    private ClassFile classFile;
    private String fullTestName;
    private Class<ObjectUnderTest.FullTestClass> aClass;

    @Before
    public void init() throws IOException {
        aClass = ObjectUnderTest.FullTestClass.class;
        classFile = readClass(aClass);
        fullTestName = ObjectUnderTest.FullTestClass.class.getName();
    }

    @Test
    public void shouldConvertClassFileToDescriptor() {
        assertThat(toTypeDescriptor(classFile), equalTo(fullTestName));
    }

    @Test
    public void shouldConvertFieldToDescriptor() {
        List<FieldInfo> fields = classFile.getFields();
        FieldInfo foo = Collections2.filter(fields, new Predicate<FieldInfo>() {
            public boolean apply(FieldInfo input) {
                return input.getName().equals("foo");
            }
        }).iterator().next();

        assertThat(tofieldDescriptor(classFile, foo), equalTo(fullTestName + ".foo"));
    }

    @Test
    public void shouldConvertSimpleMethodToDescriptor() {
        MethodInfo bar = classFile.getMethod("bar");
        assertThat(toMethodDescriptor(classFile, bar), equalTo(fullTestName + ".bar()"));
    }

    @Test
    public void shouldConvertParameterMethodToDescriptor() {
        MethodInfo bar = classFile.getMethod("fooBar");
        assertThat(toMethodDescriptor(classFile, bar), equalTo(fullTestName + ".fooBar(java.lang.String)"));    }

    @Test
    public void shouldConvertTypeDescriptorToClass() {
        String typeDescriptor = toTypeDescriptor(classFile);
        assertThat((Class<ObjectUnderTest.FullTestClass>) fromTypeDescriptor(typeDescriptor), equalTo(ObjectUnderTest.FullTestClass.class));
    }

    @Test
    public void shouldConvertFieldDescriptorToField() throws NoSuchFieldException {
        Field foo = aClass.getDeclaredField("foo");
        assertThat(fromFieldDescriptor(fullTestName + ".foo"), is(foo));
    }
}
