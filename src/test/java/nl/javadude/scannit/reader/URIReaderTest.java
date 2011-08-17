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

import com.google.common.base.Function;
import de.schlichtherle.truezip.file.TFile;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import static com.google.common.collect.Lists.transform;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class URIReaderTest {

    private URIReader uriReader;

    @Before
    public void init() {
        uriReader = new URIReader();
    }

    @Test
    public void shouldReturnEmptyListWhenPassedAFileInsteadOfADirectoryUri() throws URISyntaxException {
        URL urlToThisClassFile = Thread.currentThread().getContextClassLoader().getResource("nl/javadude/scannit/reader/URIReaderTest.class");
        List<TFile> tFiles = uriReader.listFiles(urlToThisClassFile.toURI());
        assertThat(tFiles.size(), is(1));
    }

    @Test
    public void shouldScanInJarFile() throws IOException, URISyntaxException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("empty.jar");
        assertThat(resource, notNullValue());
        List<TFile> tFiles = uriReader.listFiles(resource.toURI());
        assertThat(tFiles.size(), is(2));
        assertThat(transform(tFiles, new Function<TFile, String>() {
            public String apply(TFile input) {
                return input.getPath();
            }
        }), hasItem(endsWith("Empty.java")));
    }

	@Test
	public void shouldScanInJarFileAsUriScheme() throws IOException, URISyntaxException {
	    URL resource = Thread.currentThread().getContextClassLoader().getResource("empty.jar");
		// Convert it to a jar:file:...!/ URI (this normally happens in ClasspathReader)
		String file = "jar:file:" + resource.getFile() + "!/";
	    assertThat(resource, notNullValue());
	    List<TFile> tFiles = uriReader.listFiles(new URI(file));
	    assertThat(tFiles.size(), is(2));
	}

	@Ignore("Can be fixed when http://java.net/jira/browse/TRUEZIP-154 is fixed")
	@Test
	// Note: This happens with for example Gradle 1.0-milstone-4, which generates cached artifacts with random names on the classpath
	public void shouldScanInJarFileWithoutDot() throws Exception {
		URL resource = Thread.currentThread().getContextClassLoader().getResource("emptyjarwithoutdot");
		// Convert it to a jar:file:...!/ URI (this normally happens in ClasspathReader)
		String file = "jar:file:" + resource.getFile() + "!/";
		assertThat(resource, notNullValue());
		List<TFile> tFiles = uriReader.listFiles(new URI(file));
		assertThat(tFiles.size(), is(2));
	}

    @Test
    public void shouldNotScanNonTopLevelArchives() throws URISyntaxException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("folder");
        assertThat(resource, notNullValue());
        List<TFile> tFiles = uriReader.listFiles(resource.toURI());
        List<String> paths = transform(tFiles, new Function<TFile, String>() {
            public String apply(TFile input) {
                return input.getPath();
            }
        });
        assertThat(paths, not(hasItem(endsWith("Empty.java"))));
        assertThat(paths, hasItem(endsWith("foo.properties")));
        assertThat(tFiles.size(), is(1));
    }
}
