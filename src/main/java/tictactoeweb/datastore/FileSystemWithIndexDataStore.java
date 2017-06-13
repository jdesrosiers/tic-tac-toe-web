package tictactoeweb.datastore;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.flint.datastore.DataStoreException;
import org.flint.datastore.FileSystemDataStore;
import org.flint.exception.NotFoundHttpException;

public class FileSystemWithIndexDataStore extends FileSystemDataStore {
    public FileSystemWithIndexDataStore(final Path path) {
        super(path, FileSystemWithIndexDataStore::indexFile);
    }

    private static InputStream indexFile(final Path indexPath) throws DataStoreException {
        try {
            if (!Files.exists(Paths.get(indexPath + "/index.html"))) {
                throw new NotFoundHttpException();
            } else {
                return Files.newInputStream(Paths.get(indexPath + "/index.html"));
            }
        } catch (IOException ioe) {
            throw new DataStoreException(ioe);
        }
    }
}
