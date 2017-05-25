package tictactoeweb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

import javaslang.control.Option;

import org.flint.request.Method;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.response.Response;

public class SchemaControllerTest {

    @Test
    public void isShouldSetTheContentTypeToApplicationSchemaJson() throws IOException {
        SchemaController schemaController = new SchemaController(Paths.get("src/test/resources"));
        Request request = new Request(Method.GET, new OriginForm("/schema/test.json"));
        Response response = schemaController.get(request);

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/schema+json")));
    }

}
