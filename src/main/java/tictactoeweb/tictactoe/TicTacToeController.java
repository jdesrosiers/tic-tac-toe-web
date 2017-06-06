package tictactoeweb.tictactoe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javaslang.control.Try;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;

import org.flint.controller.Controller;
import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;
import org.flint.exception.BadRequestHttpException;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

import json.Json;
import tictactoeweb.schema.SchemaStore;
import tictactoeweb.tictactoe.model.Game;

public class TicTacToeController {
    private final Controller controller;
    private final DataStore dataStore;
    private final SchemaStore schemaStore;

    public TicTacToeController(final DataStore dataStore, final DataStore schemaStore) {
        this.controller = new Controller(dataStore);
        this.dataStore = dataStore;
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

        final JsonNode create = Json.parse(request.getBody());
        final Game game = new Game(create.at("/playerX").textValue(), create.at("/playerO").textValue());

        return writeGame(request, request.getPath() + "/1.json", game);
    }

    public Response get(final Request request) throws DataStoreException {
        final Response response = controller.get(request);
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Link", "</schema/game.json>; rel=describedby");

        return response;
    }

    public Response play(final Request request) throws DataStoreException, IOException {
        final String position = Json.parse(request.getBody()).at("/position").textValue();
        final Game game = Json.parseAs(dataStore.fetch(request.getPath()), Game.class);

        if (!game.canPlay(position)) {
            throw new BadRequestHttpException();
        } else {
            game.play(position);
        }

        return writeGame(request, request.getPath(), game);
    }

    private Response writeGame(Request request, String location, Game game) throws DataStoreException, JsonProcessingException {
        final Response response = controller.write(request, location, Json.stringify(game));
        response.setStatusCode(StatusCode.SEE_OTHER);
        response.setHeader("Location", location);

        return response;
    }
}
