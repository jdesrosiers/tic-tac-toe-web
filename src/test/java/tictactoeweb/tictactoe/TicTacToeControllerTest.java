package tictactoeweb.tictactoe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javaslang.control.Option;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import org.flint.exception.BadRequestHttpException;
import org.flint.request.Method;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

import tictactoeweb.schema.SchemaStore;

public class TicTacToeControllerTest {

    private TicTacToeController getController(Path dataPath) {
        final Path schemaPath = Paths.get("src/main");
        return new TicTacToeController(dataPath, new SchemaStore(schemaPath));
    }

    @Test
    public void itShouldGetTheIndexWithHyperSchema() throws IOException {
        final TicTacToeController ticTacToeController = getController(Paths.get("."));
        final Request request = new Request(Method.GET, new OriginForm("/tictactoe"));
        final Response response = ticTacToeController.index(request);

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/json")));
        assertThat(response.getHeader("Link"), equalTo(Option.of("</schema/index.json>; rel=describedby")));
    }

    @Test
    public void itShouldCreateANewGame() throws IOException, ProcessingException {
        final TicTacToeController ticTacToeController = getController(Paths.get("src/test/resources"));
        final Request request = new Request(Method.POST, new OriginForm("/tictactoe"));
        request.setBody("{ \"playerX\": \"human\", \"playerO\": \"minimax\" }");
        final Response response = ticTacToeController.create(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.SEE_OTHER));
        assertThat(response.getHeader("Location"), equalTo(Option.of("/tictactoe/1.json")));
    }

    @Test(expected=BadRequestHttpException.class)
    public void itShould400IfCreateGameRequestIsInvalid() throws IOException, ProcessingException {
        final TicTacToeController ticTacToeController = getController(Paths.get("src/test/resources"));
        final Request request = new Request(Method.POST, new OriginForm("/tictactoe"));
        request.setBody("{ \"playerX\": \"invalid\", \"playerO\": \"minimax\" }");
        ticTacToeController.create(request);
    }

    @Test
    public void itShouldGetAGame() throws IOException {
        final TicTacToeController ticTacToeController = getController(Paths.get("src/test/resources"));
        final Request request = new Request(Method.GET, new OriginForm("/tictactoe/1.json"));
        final Response response = ticTacToeController.index(request);

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/json")));
    }

}
