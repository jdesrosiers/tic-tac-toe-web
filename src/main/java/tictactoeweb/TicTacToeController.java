package tictactoeweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import scala.Symbol;

import javaslang.control.Try;

import org.cobspec.controller.FileSystemController;
import org.flint.exception.BadRequestHttpException;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;

import tictactoeweb.model.CreateGame;
import tictactoeweb.model.Game;

class TicTacToeController {
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

    public Response create(final Request request) throws IOException, ProcessingException {
        final JsonSchema schema = schemaStore.getSchema("/schema/create.json");
        Try.of(() -> schema.validate(Json.parse(request.getBody())))
            .filter(ProcessingReport::isSuccess)
            .getOrElseThrow(() -> new BadRequestHttpException());

        final CreateGame create = Json.parseAs(request.getBody(), CreateGame.class);
        final String json = Json.stringify(create.toGame());
        final Response response = fileSystemController.write(request, Paths.get(request.getPath() + "/1.json"), json);
        response.setStatusCode(StatusCode.SEE_OTHER);

        return response;
    }

    public Response get(final Request request) throws IOException {
        final Response response = fileSystemController.get(request);
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Link", "</schema/game.json>; rel=describedby");

        return response;
    }
}
