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

    public Set<URI> findURIs() {
        logger.debug("Finding all URIs from the classloader");
        return findURIs(Thread.currentThread().getContextClassLoader());
    }

    private Set<URI> findURIs(ClassLoader contextClassLoader) {
        if (contextClassLoader.getParent() == null || !(contextClassLoader instanceof URLClassLoader))
            return newHashSet();

        URL[] urLs = ((URLClassLoader) contextClassLoader).getURLs();
        HashSet<URI> uris = newHashSet(Lists.transform(newArrayList(Iterators.forArray(urLs)), url2uri));
        uris.addAll(findURIs(contextClassLoader.getParent()));
        return uris;
    }

    public Set<URI> findBaseURIs(String prefix) {
        Set<URI> result = newHashSet();

        for (URI urI : findURIs(prefix)) {
            for (URI base : findURIs()) {
                if (urI.getPath().startsWith(base.getPath())) {
                    result.add(base);
                }
            }
        }

        return result;
    }

    private static final Logger logger = LoggerFactory.getLogger(ClasspathReader.class);
}
