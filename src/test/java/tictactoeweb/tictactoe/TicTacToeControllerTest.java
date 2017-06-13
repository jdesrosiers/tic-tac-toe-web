package tictactoeweb.tictactoe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javaslang.Tuple;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;
import org.flint.datastore.FileSystemDataStore;
import org.flint.datastore.MapDataStore;
import org.flint.exception.BadRequestHttpException;
import org.flint.request.Method;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;
import org.util.FileSystem;

import json.Json;
import tictactoeweb.schema.SchemaStore;

public class TicTacToeControllerTest {
    private DataStore dataStore;
    private final String newGame = "{\"id\":1,\"playerX\":\"human\",\"playerO\":\"minimax\",\"board\":{\"player\":\"X\",\"xs\":[],\"os\":[]},\"state\":\"inProgress\",\"player\":\"human\"}";
    private final String game = "{\"id\":1,\"playerX\":\"human\",\"playerO\":\"minimax\",\"board\":{\"player\":\"O\",\"xs\":[\"center\"],\"os\":[]},\"state\":\"inProgress\",\"player\":\"minimax\"}";

    private TicTacToeController getController(Map<String, String> data) {
        this.dataStore = new MapDataStore(data);
        final DataStore schemaStore = new FileSystemDataStore(Paths.get("src/main"));
        return new TicTacToeController(dataStore, schemaStore);
    }

    @Test
    public void itShouldGetTheIndexWithHyperSchema() throws DataStoreException {
        final TicTacToeController ticTacToeController = getController(HashMap.empty());
        final Request request = new Request(Method.GET, new OriginForm("/tictactoe"));
        final Response response = ticTacToeController.index(request);

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/json")));
        assertThat(response.getHeader("Link"), equalTo(Option.of("</schema/index.json>; rel=describedby")));
    }

    @Test
    public void itShouldCreateANewGame() throws DataStoreException, IOException, ProcessingException {
        final TicTacToeController ticTacToeController = getController(HashMap.empty());
        final Request request = new Request(Method.POST, new OriginForm("/tictactoe"));
        request.setBody("{\"playerX\":\"human\",\"playerO\":\"minimax\"}");
        final Response response = ticTacToeController.create(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.SEE_OTHER));
        assertThat(response.getHeader("Location"), equalTo(Option.of("/tictactoe/1.json")));
        assertThat(FileSystem.inputStreamToString(dataStore.fetch("/tictactoe/1.json")), equalTo(newGame));
    }

    @Test(expected=BadRequestHttpException.class)
    public void itShould400IfCreateGameRequestIsInvalid() throws DataStoreException, IOException, ProcessingException {
        final TicTacToeController ticTacToeController = getController(HashMap.empty());
        final Request request = new Request(Method.POST, new OriginForm("/tictactoe"));
        request.setBody("{\"playerX\":\"invalid\",\"playerO\":\"minimax\"}");
        ticTacToeController.create(request);
    }

    @Test
    public void itShouldGetAGame() throws DataStoreException, IOException {
        final Map<String, String> data = HashMap.ofEntries(
            Tuple.of("/tictactoe/1.json", newGame)
        );
        final TicTacToeController ticTacToeController = getController(data);
        final Request request = new Request(Method.GET, new OriginForm("/tictactoe/1.json"));
        final Response response = ticTacToeController.index(request);

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/json")));
        assertThat(FileSystem.inputStreamToString(dataStore.fetch("/tictactoe/1.json")), equalTo(newGame));
    }

    @Test
    public void itShouldPlayAPosition() throws DataStoreException, IOException {
        final Map<String, String> data = HashMap.ofEntries(
            Tuple.of("/tictactoe/1.json", newGame)
        );
        final TicTacToeController ticTacToeController = getController(data);
        final Request request = new Request(Method.POST, new OriginForm("/tictactoe/1.json"));
        request.setHeader("ContentType", "application/json");
        request.setBody("{\"position\":\"center\"}");
        final Response response = ticTacToeController.play(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.SEE_OTHER));
        assertThat(response.getHeader("Location"), equalTo(Option.of("/tictactoe/1.json")));
        assertThat(FileSystem.inputStreamToString(dataStore.fetch("/tictactoe/1.json")), equalTo(game));
    }

    @Test(expected=BadRequestHttpException.class)
    public void itShould400WhenTryingToPlayAnInvalidMove() throws DataStoreException, IOException {
        final Map<String, String> data = HashMap.ofEntries(
            Tuple.of("/tictactoe/1.json", newGame)
        );
        final TicTacToeController ticTacToeController = getController(data);
        final Request request = new Request(Method.POST, new OriginForm("/tictactoe/1.json"));
        request.setHeader("ContentType", "application/json");
        request.setBody("{\"position\":\"invalid\"}");
        final Response response = ticTacToeController.play(request);
    }

    @Test
    public void itShouldPlayWithMinimax() throws DataStoreException, IOException {
        final Map<String, String> data = HashMap.ofEntries(
            Tuple.of("/tictactoe/1.json", game)
        );
        final TicTacToeController ticTacToeController = getController(data);
        final Request request = new Request(Method.POST, new OriginForm("/tictactoe/1.json"));
        request.setHeader("ContentType", "application/json");
        request.setBody("{}");
        final Response response = ticTacToeController.play(request);

        JsonNode body = Json.parse(dataStore.fetch("/tictactoe/1.json"));
        assertThat(response.getStatusCode(), equalTo(StatusCode.SEE_OTHER));
        assertThat(response.getHeader("Location"), equalTo(Option.of("/tictactoe/1.json")));
        assertThat(body.at("/board/xs/0").asText(), isOneOf("topRight", "topMiddle", "topLeft", "middleRight", "center", "middleLeft", "bottomRight", "bottomMiddle", "bottomLeft"));
    }

}
