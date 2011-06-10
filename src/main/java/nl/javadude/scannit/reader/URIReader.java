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
import de.schlichtherle.truezip.file.TFileInputStream;
import javassist.bytecode.ClassFile;
import nl.javadude.scannit.Configuration;
import nl.javadude.scannit.metadata.JavassistHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Read an URI into a File list, using TrueZip to scan through package.
 */
public class URIReader {
    /**
     * Convert a URI into a recursive list of all the File entries in it and any of its directories.
     *
     * @param uri The URI to read
     * @return a List of TFile of all the entries in the URI.
     */
    public List<TFile> listFiles(URI uri) {
        TFile tFile = null;
        if (uri.getPath() != null) {
            // if the URI has a path, this means it is a real file, use the detection of the path, and not look at the scheme
            tFile = new TFile(uri.getPath());
        } else {
            // Detect based on the scheme by passing in the uri.
            tFile = new TFile(uri);
        }
        List<TFile> files = newArrayList();

        if (tFile.isArchive()) {
            list(tFile, files, true);
        } else {
            list(tFile, files, false);
        }

        return files;
    }

    private void list(TFile tFile, List<TFile> files, boolean scanInArchives) {
        boolean isArchive = tFile.isArchive();
        boolean isNormalDir = tFile.isDirectory() && !isArchive;
        boolean hasFiles = tFile.listFiles() != null;

        if ((isNormalDir && hasFiles) || isArchive && scanInArchives && hasFiles) {
            logger.trace("Listing directory/archive of file: {}", tFile);
            for (TFile file : tFile.listFiles()) {
                list(file, files, false);
            }
        } else if (tFile.isFile() || tFile.isEntry()) {
            logger.trace("Found file/entry {}", tFile);
            files.add(tFile);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(URIReader.class);
}

