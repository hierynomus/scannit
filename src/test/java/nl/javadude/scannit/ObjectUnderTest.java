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

