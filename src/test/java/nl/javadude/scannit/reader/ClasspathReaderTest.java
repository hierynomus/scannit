package nl.javadude.scannit.reader;

import org.junit.Test;

import java.net.URI;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class ClasspathReaderTest {

    @Test
    public void shouldFindTestClass() {
        Set<URI> urIs = new ClasspathReader().findURIs("nl.javadude");
        assertThat(urIs, notNullValue());
        assertThat(urIs.size(), not(equalTo(0)));
        for (URI urI : urIs) {
            assertThat(urI.getPath(), containsString("nl/javadude"));
        }
    }

    @Test
    public void shouldFindUrisFromContextClassloader() {
        Set<URI> urIs = new ClasspathReader().findURIs();
        assertThat(urIs, notNullValue());
        assertThat(urIs.size(), not(equalTo(0)));
    }

    @Test
    public void shouldFindBaseURIs() {
        Set<URI> urIs = new ClasspathReader().findURIs("nl.javadude");
        Set<URI> baseUris = new ClasspathReader().findBaseURIs("nl.javadude");
        assertThat(baseUris.size(), equalTo(urIs.size()));
        
    }
}
