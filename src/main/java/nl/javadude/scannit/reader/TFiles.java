package nl.javadude.scannit.reader;

import java.net.URI;

import de.schlichtherle.truezip.file.TVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.fs.FsSyncException;

final class TFiles {

    private TFiles() {
        // Do not instantiate
    }

    public static TFile tFile(URI uri) {
        if (uri.getPath() != null) {
            // if the URI has a path, this means it is a real file, use the detection of the path, and not look at the scheme
            return new TFile(uri.getPath());
        } else {
            return new TFile(uri);
        }
    }

    public static void umountQuietly(TFile tFile) {
        try {
            if (tFile.isArchive() && tFile.getEnclArchive() == null) {
                TVFS.umount(tFile);
            }
        } catch (FsSyncException e) {
            logger.error("Could not umount {}, continuing", tFile);
            logger.debug("Exception was: ", e);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(TFiles.class);
}
