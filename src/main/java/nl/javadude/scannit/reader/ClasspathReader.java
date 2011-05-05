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
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Reads the classLoader and returns URIs
 */
public class ClasspathReader {

    private Function<URL, URI> url2uri = new Function<URL, URI>() {
        public URI apply(URL url) {
            try {
                logger.debug("Encountered URL: {}", url);
                return url.toURI();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Could not convert URL: " + url + " to a URI", e);
            }
        }
    };

    /**
     * Finds all URIs in the ContextClassLoader which contain a certain package.
     * @param packagePrefix The (prefix) package to search for
     * @return all matching URIs, each URI includes the package!
     */
    public Set<URI> findURIs(String packagePrefix) {
        String filePath = packagePrefix.replaceAll("\\.", "/");
        logger.debug("Finding resources for prefix: {}", filePath);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        List<URI> uris = newArrayList();
        try {
            Enumeration<URL> resources = contextClassLoader.getResources(filePath);
            uris = Lists.transform(Lists.newArrayList(Iterators.forEnumeration(resources)), url2uri);
        } catch (IOException e) {
            throw new IllegalStateException("ask Jeroen", e);
        }
        return newHashSet(uris);
    }

    /**
     * Returns all URIs in the ContextClassLoader and any of its parent classloaders.
     * @return
     */
    public Set<URI> findURIs() {
        logger.debug("Finding all URIs from the classloader");
        return findURIs(Thread.currentThread().getContextClassLoader());
    }

    private Set<URI> findURIs(ClassLoader contextClassLoader) {
        if (contextClassLoader == null)
            return newHashSet();
        else if (!(contextClassLoader instanceof URLClassLoader))
            return findURIs(contextClassLoader.getParent());

        URL[] urLs = ((URLClassLoader) contextClassLoader).getURLs();
        HashSet<URI> uris = newHashSet(Lists.transform(newArrayList(Iterators.forArray(urLs)), url2uri));
        uris.addAll(findURIs(contextClassLoader.getParent()));
        return uris;
    }

    /**
     * Finds all the base URIs which contain a (prefix) package. The URIs are returned without the actual package, opposed to a call to {@link #findURIs(String)}
     *
     * @param prefix The package prefix to scan for.
     * @return the base URIs
     */
    public Set<URI> findBaseURIs(String prefix) {
        Set<URI> result = newHashSet();

        Set<URI> urIs = findURIs(prefix);
        Set<URI> baseUris = findURIs();
        for (URI urI : urIs) {
            for (URI base : baseUris) {
                if (urI.getSchemeSpecificPart().contains(base.getSchemeSpecificPart())) {
                    result.add(base);
                }
            }
        }

        logger.debug("Found base uris: " + result);
        return result;
    }

    private static final Logger logger = LoggerFactory.getLogger(ClasspathReader.class);
}

