package tictactoeweb;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import java.nio.file.Paths;

import javaslang.control.Option;

import org.flint.Application;
import org.flint.request.OriginForm;
import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class TicTacToeWebTest {

    @Test
    public void itShouldServeTheIndexPage() {
        Application app = TicTacToeWeb.build(Paths.get("web"));
        Request request = new Request(Method.GET, new OriginForm("/"));

        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/html")));
        assertThat(response.getBodyAsString(), containsString("Tic Tac Toe"));
    }

    @Test
    public void itShouldServeAnyResourcesInTheWebFolder() {
        Application app = TicTacToeWeb.build(Paths.get("web"));
        Request request = new Request(Method.GET, new OriginForm("/app/index.js"));

        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/javascript")));
    }

}
