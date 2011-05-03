package nl.javadude.scannit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ObjectUnderTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClassAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface MethodAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface FieldAnnotation {
    }


    @ObjectUnderTest.ClassAnnotation
    public class TestClass {
    }


    public class TestClassWithAnnotatedMethod {
        @MethodAnnotation
        public String foo() {
            return "foo";
        }
    }

    @ClassAnnotation
    public class FullTestClass {
        @FieldAnnotation private String foo;

        @MethodAnnotation
        public void bar() {}

        @MethodAnnotation
        public String fooBar(String baz) {
            return baz;
        }
    }
}
