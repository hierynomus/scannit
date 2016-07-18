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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Reads the classLoader and returns URIs
 */
public class ClasspathReader {

    /**
     * Finds all URIs in the ContextClassLoader which contain a certain package.
     *
     * @param packagePrefix The (prefix) package to search for
     * @return all matching URIs, each URI includes the package!
     */
    public Set<URI> findURIs(String packagePrefix) {
        String filePath = packagePrefixToPath(packagePrefix);
        logger.debug("Finding resources for prefix: {}", filePath);
        ClassLoader contextClassLoader = getClassLoader();
        List<URI> uris = new ArrayList<URI>();
        try {
            Enumeration<URL> resources = contextClassLoader.getResources(filePath);
            while(resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try {
                    logger.debug("Encountered URL: {}", url);
                    uris.add(url.toURI());
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("Could not convert URL: " + url + " to a URI", e);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not fetch the resources matching " + filePath + " from the context classloader.", e);
        }
        return new HashSet<URI>(uris);
    }

    private String packagePrefixToPath(String packagePrefix) {
        return packagePrefix.replaceAll("\\.", "/");
    }

    /**
     * Extracted for testing purposes, so we can inject with a mock classloader.
     *
     * @return The thread context classloader.
     */
    protected ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Finds all the base URIs which contain a (prefix) package. The URIs are returned without the actual package, opposed to a call to {@link #findURIs(String)}
     *
     * @param prefix The package prefix to scan for.
     * @return the base URIs
     */
    public Set<URI> findBaseURIs(String prefix) {
        Set<URI> result = new HashSet<URI>();

        Set<URI> urIs = findURIs(prefix);
        String path = packagePrefixToPath(prefix);
        for (URI urI : urIs) {
            String schemeSpecificPart = urI.getSchemeSpecificPart();
            int l = path.length();
            if (schemeSpecificPart.endsWith("/") && !path.endsWith("/")) {
                l++;
	        }
            String based = schemeSpecificPart.substring(0, schemeSpecificPart.length() - l);
            try {
                result.add(new URI(urI.getScheme(), based, urI.getFragment()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        logger.debug("Found base uris: {}", result);
        return result;
    }

    private static final Logger logger = LoggerFactory.getLogger(ClasspathReader.class);
}

