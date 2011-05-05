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

import de.schlichtherle.truezip.file.TFile;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class URIReaderTest {
    @Test
    public void shouldReturnEmptyListWhenPassedAFileInsteadOfADirectoryUri() throws URISyntaxException {
        URL urlToThisClassFile = Thread.currentThread().getContextClassLoader().getResource("nl/javadude/scannit/reader/URIReaderTest.class");
        URIReader reader = new URIReader();
        List<TFile> tFiles = reader.listFiles(urlToThisClassFile.toURI());
        assertThat(tFiles.size(), is(1));
    }

}
