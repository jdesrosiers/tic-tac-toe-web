package tictactoeweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javaslang.jackson.datatype.JavaslangModule;

import javaslang.control.Try;

import org.cobspec.controller.FileSystemController;
import org.flint.exception.BadRequestHttpException;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

import com.github.fge.jsonschema.core.report.ProcessingReport;

import tictactoeweb.model.CreateGame;

class TicTacToeController {
    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaslangModule());

    private final FileSystemController fileSystemController;
    private final SchemaStore schemaStore;

    public TicTacToeController(final Path path, final SchemaStore schemaStore) {
        this.fileSystemController = new FileSystemController(path);
        this.schemaStore = schemaStore;
    }

    public Response index(final Request request) throws IOException {
        final Response response = fileSystemController.get(request);
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Link", "</schema/index.json>; rel=describedby");

        return response;
    }

    public Response create(final Request request) throws IOException {
        final JsonNode createGame = mapper.readTree(request.getBody());

        schemaStore.get("/schema/create.json")
            .flatMap(schema -> Try.of(() -> schema.validate(createGame)))
            .filter(ProcessingReport::isSuccess)
            .getOrElseThrow(() -> new BadRequestHttpException());

        final CreateGame create = mapper.readValue(request.getBody(), CreateGame.class);

        final String json = mapper.writer().writeValueAsString(create.toGame());
        final Response response = fileSystemController.write(request, Paths.get(request.getPath() + "/1.json"), json);
        response.setStatusCode(StatusCode.SEE_OTHER);

        return response;
    }

    public Response get(final Request request) throws IOException {
        final Response response = fileSystemController.get(request);
        response.setHeader("Content-Type", "application/json");

        return response;
    }
}
