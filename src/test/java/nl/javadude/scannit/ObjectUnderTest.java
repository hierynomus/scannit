package nl.javadude.scannit;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ObjectUnderTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClassAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface InheritedAnnotation {}

    @Retention(RetentionPolicy.RUNTIME)
    public @interface MethodAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface FieldAnnotation {
    }


    @ObjectUnderTest.ClassAnnotation
    public class AnnotatedTestClass {
    }

    public class TestClassWithAnnotatedField {
        @FieldAnnotation
        private String foo;
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

        @MethodAnnotation
        public int foozBar(int baz) {
            return baz;
        }

        @MethodAnnotation
        public int[] fooArBar(int[] baz) {
            return baz;
        }

        @MethodAnnotation
        public String[] fooAr2Bar(String[] baz) {
            return baz;
        }
    }

    @InheritedAnnotation
    public class FooClass {}

    public class BarClass extends FooClass {}

    public interface Minor {}
    public interface Person {}
    public class Parent implements Person {}
    public class Child extends Parent {}
    public class GrandChild extends Child implements Minor {}
}
