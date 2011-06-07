package nl.javadude.excluded;

import nl.javadude.scannit.ObjectUnderTest;

public interface AnotherObjectNotTested {

    @ObjectUnderTest.ClassAnnotation
    public class AnnotatedNotTestClass {
    }

    public class NotTestClassWithAnnotatedField {
        @ObjectUnderTest.FieldAnnotation
        private String foo;
    }

}
