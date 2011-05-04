package nl.javadude.scannit;

import nl.javadude.scannit.filter.Filter;
import nl.javadude.scannit.scanner.SubTypeScanner;
import nl.javadude.scannit.scanner.TypeAnnotationScanner;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class ScannitTest {

    private Scannit scannit;

    @Before
    public void init() {
        SubTypeScanner subTypeScanner = new SubTypeScanner();
        Filter include = Filter.include("nl\\.javadude\\.scannit.*");
        subTypeScanner.addFilter(include);
        TypeAnnotationScanner typeAnnotationScanner = new TypeAnnotationScanner();
        typeAnnotationScanner.addFilter(include);
        Configuration config = Configuration.config().with(subTypeScanner, typeAnnotationScanner).scan("nl.javadude.scannit");
        scannit = new Scannit(config);
    }

    @Test
    public void shouldHaveFoundClassAnnotation() {
        Set typesAnnotatedWith = scannit.getTypesAnnotatedWith(ObjectUnderTest.ClassAnnotation.class);
        assertThat((Iterable<Class<ObjectUnderTest.AnnotatedTestClass>>) typesAnnotatedWith, hasItem(ObjectUnderTest.AnnotatedTestClass.class));
        assertThat((Iterable<Class<ObjectUnderTest.FullTestClass>>) typesAnnotatedWith, hasItem(ObjectUnderTest.FullTestClass.class));
    }
    
    @Test
    public void shouldHaveFoundInheritedAnnotation() {
        Set typesAnnotatedWith = scannit.getTypesAnnotatedWith(ObjectUnderTest.InheritedAnnotation.class);
        assertThat((Iterable<Class<ObjectUnderTest.FooClass>>) typesAnnotatedWith, hasItem(ObjectUnderTest.FooClass.class));
        assertThat((Iterable<Class<ObjectUnderTest.BarClass>>) typesAnnotatedWith, hasItem(ObjectUnderTest.BarClass.class));
    }


}
