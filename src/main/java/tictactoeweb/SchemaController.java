package tictactoeweb;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import org.flint.exception.NotFoundHttpException;
import org.flint.request.Request;
import org.flint.response.Response;

public class SchemaController {
    private final SchemaStore schemaStore;

    public SchemaController(final SchemaStore schemaStore) {
        this.schemaStore = schemaStore;
    }

    public Response get(final Request request) throws IOException, ProcessingException {
        if (!schemaStore.hasSchema(request.getPath())) {
            throw new NotFoundHttpException();
        }

        final String schema = schemaStore.getString(request.getPath());

        final Response response = Response.create();
        response.setBody(schema);
        response.setHeader("Content-Type", "application/schema+json");

        return response;
    }
}
