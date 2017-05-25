package tictactoeweb;

import java.io.IOException;
import java.nio.file.Path;

import org.cobspec.controller.FileSystemController;
import org.flint.request.Request;
import org.flint.response.Response;

public class SchemaController {
    final private FileSystemController fileSystemController;

    public SchemaController(final Path path) {
        this.fileSystemController = new FileSystemController(path);
    }

    public Response get(final Request request) throws IOException {
        Response response = fileSystemController.get(request);
        response.setHeader("Content-Type", "application/schema+json");

        return response;
    }
}
