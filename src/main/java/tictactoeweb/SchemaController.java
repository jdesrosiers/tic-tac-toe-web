package tictactoeweb;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javaslang.jackson.datatype.JavaslangModule;

import org.flint.exception.NotFoundHttpException;
import org.flint.request.Request;
import org.flint.response.Response;

public class SchemaController {
    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaslangModule());

    private final SchemaStore schemaStore;

    public SchemaController(final SchemaStore schemaStore) {
        this.schemaStore = schemaStore;
    }

    public Response get(final Request request) throws IOException, JsonProcessingException {
        final JsonNode schema = schemaStore.getAsJson(request.getPath())
            .getOrElseThrow(() -> new NotFoundHttpException());

        final Response response = Response.create();
        response.setBody(mapper.writer().writeValueAsString(schema));
        response.setHeader("Content-Type", "application/schema+json");

        return response;
    }
}
