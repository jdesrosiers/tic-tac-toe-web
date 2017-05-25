package tictactoeweb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

import javaslang.control.Option;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import org.flint.exception.BadRequestHttpException;
import org.flint.request.Method;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class TicTacToeControllerTest {

    @Test
    public void isShouldGetTheIndexWithHyperSchema() throws IOException, ProcessingException {
        TicTacToeController ticTacToeController = new TicTacToeController(Paths.get("."));
        Request request = new Request(Method.GET, new OriginForm("/tictactoe"));
        Response response = ticTacToeController.index(request);

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/json")));
        assertThat(response.getHeader("Link"), equalTo(Option.of("</schema/index.json>; rel=describedby")));
    }

    @Test
    public void isShouldCreateANewGame() throws IOException, ProcessingException {
        TicTacToeController ticTacToeController = new TicTacToeController(Paths.get("src/test/resources"));
        Request request = new Request(Method.POST, new OriginForm("/tictactoe"));
        request.setBody("{ \"playerX\": \"human\", \"playerO\": \"minimax\" }");
        Response response = ticTacToeController.create(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.SEE_OTHER));
        assertThat(response.getHeader("Location"), equalTo(Option.of("/tictactoe/1.json")));
    }

    @Test(expected=BadRequestHttpException.class)
    public void isShould400IfCreateGameRequestIsInvalid() throws IOException, ProcessingException {
        TicTacToeController ticTacToeController = new TicTacToeController(Paths.get("src/test/resources"));
        Request request = new Request(Method.POST, new OriginForm("/tictactoe"));
        request.setBody("{ \"playerX\": \"invalid\", \"playerO\": \"minimax\" }");
        Response response = ticTacToeController.create(request);
    }

    @Test
    public void isShouldGetAGame() throws IOException, ProcessingException {
        TicTacToeController ticTacToeController = new TicTacToeController(Paths.get("src/test/resources"));
        Request request = new Request(Method.GET, new OriginForm("/tictactoe/1.json"));
        Response response = ticTacToeController.index(request);

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/json")));
    }

}
