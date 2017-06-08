package tictactoeweb.tictactoe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import scala.Symbol;

import javaslang.control.Try;

import org.flint.controller.Controller;
import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;
import org.flint.datastore.FileSystemDataStore;
import org.flint.exception.BadRequestHttpException;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;

import tictactoeweb.schema.SchemaStore;
import tictactoeweb.tictactoe.model.CreateGame;
import tictactoeweb.tictactoe.model.Game;
import json.Json;

public class TicTacToeController {
    private final Controller controller;
    private final SchemaStore schemaStore;

    public TicTacToeController(final DataStore dataStore, final DataStore schemaStore) {
        this.controller = new Controller(dataStore);
        this.schemaStore = new SchemaStore(schemaStore);
    }

    public Response index(final Request request) throws DataStoreException {
        final Response response = Response.create();
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Link", "</schema/index.json>; rel=describedby");
        response.setBody("{\"title\":\"Tic Tac Toe API\"}");

        return response;
    }

    public Response create(final Request request) throws DataStoreException, IOException, ProcessingException {
        final JsonSchema schema = schemaStore.fetchSchema("/schema/create.json");
        Try.of(() -> schema.validate(Json.parse(request.getBody())))
            .filter(ProcessingReport::isSuccess)
            .getOrElseThrow(() -> new BadRequestHttpException());

        final CreateGame create = Json.parseAs(request.getBody(), CreateGame.class);
        final String json = Json.stringify(create.toGame());
        final Response response = controller.write(request, request.getPath() + "/1.json", json);
        response.setStatusCode(StatusCode.SEE_OTHER);

        return response;
    }

    public Response get(final Request request) throws DataStoreException {
        final Response response = controller.get(request);
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Link", "</schema/game.json>; rel=describedby");

        return response;
    }
}
