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

package nl.javadude.scannit.reader;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
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

    @Test
    public void shouldCorrectlyDeduceBaseUriForJarFiles() {
        TestClasspathReader testClasspathReader = new TestClasspathReader("jar:file:foo/bar/baz.jar/org/apache", "file:foo/bar/baz.jar");
        assertThat(testClasspathReader.findBaseURIs("org.apache").size(), equalTo(1));
    }

    @Test
    public void shouldCorrectlyDeduceBaseUriForDirectories() {
        TestClasspathReader testClasspathReader = new TestClasspathReader("file:foo/bar/baz/org/apache", "file:foo/bar/baz");
        assertThat(testClasspathReader.findBaseURIs("org.apache").size(), equalTo(1));
    }

    private static class TestClasspathReader extends ClasspathReader {
        private String uri;
        private String baseUri;

        private TestClasspathReader(String uri, String baseUri) {
            this.uri = uri;
            this.baseUri = baseUri;
        }

        @Override
        public Set<URI> findURIs(String packagePrefix) {
            try {
                return Sets.newHashSet(new URI(uri));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Set<URI> findURIs() {
            try {
                return Sets.newHashSet(new URI(baseUri));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

