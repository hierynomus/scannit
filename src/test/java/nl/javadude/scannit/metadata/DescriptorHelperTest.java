/* License added by: GRADLE-LICENSE-PLUGIN
 *
 *    Copyright 2011 Jeroen van Erp (jeroen@javadude.nl)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package nl.javadude.scannit.metadata;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import nl.javadude.scannit.ObjectUnderTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        assertThat(toMethodDescriptor(classFile, bar), equalTo(fullTestName + ".fooBar(java.lang.String)"));
    }

    @Test
    public void shouldConvertPrimitiveParameterMethodToDescriptor() {
        MethodInfo bar = classFile.getMethod("foozBar");
        assertThat(toMethodDescriptor(classFile, bar), equalTo(fullTestName + ".foozBar(int)"));
    }

    @Test
    public void shouldConvertPrimitiveArrayParameterMethodToDescriptor() {
        MethodInfo bar = classFile.getMethod("fooArBar");
        assertThat(toMethodDescriptor(classFile, bar), equalTo(fullTestName + ".fooArBar(int[])"));
    }

    @Test
    public void shouldConvertArrayParameterMethodToDescriptor() {
        MethodInfo bar = classFile.getMethod("fooAr2Bar");
        assertThat(toMethodDescriptor(classFile, bar), equalTo(fullTestName + ".fooAr2Bar(java.lang.String[])"));
    }

    @Test
    public void shouldConvertTypeDescriptorToClass() {
        String typeDescriptor = toTypeDescriptor(classFile);
        assertThat((Class<ObjectUnderTest.FullTestClass>) fromTypeDescriptor(typeDescriptor), equalTo(ObjectUnderTest.FullTestClass.class));
    }

    @Test
    public void shouldConvertSimpleMethodDescriptorToMethod() throws NoSuchMethodException {
        String methodDescriptor = fullTestName + ".bar()";
        Method bar = aClass.getMethod("bar");
        assertThat(fromMethodDescriptor(methodDescriptor), is(bar));
    }

    @Test
    public void shouldConvertParameterMethodDescriptorToMethod() throws NoSuchMethodException {
        String methodDescriptor = fullTestName + ".fooBar(java.lang.String)";
        Method fooBar = aClass.getMethod("fooBar", String.class);
        assertThat(fromMethodDescriptor(methodDescriptor), is(fooBar));
    }

    @Test
    public void shouldConvertPrimitiveParameterMethodDescriptorToMethod() throws NoSuchMethodException {
        String methodDescriptor = fullTestName + ".foozBar(int)";
        Method fooBar = aClass.getMethod("foozBar", int.class);
        assertThat(fromMethodDescriptor(methodDescriptor), is(fooBar));
    }

    @Test
    public void shouldConvertPrimitiveArrayParameterMethodDescriptorToMethod() throws NoSuchMethodException {
        String methodDescriptor = fullTestName + ".fooArBar(int[])";
        Method fooBar = aClass.getMethod("fooArBar", int[].class);
        assertThat(fromMethodDescriptor(methodDescriptor), is(fooBar));
    }

    @Test
    public void shouldConvertArrayParameterMethodDescriptorToMethod() throws NoSuchMethodException {
        String methodDescriptor = fullTestName + ".fooAr2Bar(java.lang.String[])";
        Method fooBar = aClass.getMethod("fooAr2Bar", String[].class);
        assertThat(fromMethodDescriptor(methodDescriptor), is(fooBar));
    }

    @Test
    public void shouldConvertFieldDescriptorToField() throws NoSuchFieldException {
        Field foo = aClass.getDeclaredField("foo");
        assertThat(fromFieldDescriptor(fullTestName + ".foo"), is(foo));
    }
}

