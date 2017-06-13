package tictactoeweb.tictactoe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javaslang.control.Try;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;

import org.jparsec.Scanners;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.error.ParserException;

import org.flint.controller.Controller;
import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;
import org.flint.exception.BadRequestHttpException;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

import tictactoeui.classic.ClassicGame;
import tictactoeui.MinimaxPlayer;
import tictactoeui.Player;

import json.Json;
import tictactoeweb.schema.SchemaStore;
import tictactoeweb.tictactoe.model.Board;
import tictactoeweb.tictactoe.model.Game;

public class TicTacToeController {
    private static final tictactoeui.Game game = new ClassicGame();

    private static final Parser<MinimaxPlayer> PARSER = Parsers.sequence(
        Scanners.string("minimax"),
        Parsers.sequence(
            Scanners.isChar(','),
            Scanners.INTEGER.map(Integer::valueOf),
            (_1, depth) -> depth
        ).optional(8),
        (minimax, depth) -> new MinimaxPlayer(game, depth)
    );

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
        String position;

        final Game game = Json.parseAs(dataStore.fetch(request.getPath()), Game.class);
        final JsonNode play = Json.parse(request.getBody());
        final String playerType = game.getPlayer();

        if (playerType.equals("human")) {
            position = play.at("/position").textValue();
            if (!game.canPlay(position)) {
                throw new BadRequestHttpException();
            }
        } else {
            position = PARSER.parse(playerType).getMove(game.getBoard()).name();
        }

        game.play(position);

        return writeGame(request, request.getPath(), game);
    }

    private Response writeGame(Request request, String location, Game game) throws DataStoreException, JsonProcessingException {
        final Response response = controller.write(request, location, Json.stringify(game));
        response.setStatusCode(StatusCode.SEE_OTHER);
        response.setHeader("Location", location);

        return response;
    }
}
