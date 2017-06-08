package tictactoeweb.schema;

import org.flint.controller.Controller;
import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;
import org.flint.request.Request;
import org.flint.response.Response;

public class SchemaController {
    private final Controller controller;

    public SchemaController(final DataStore schemaStore) {
        this.controller = new Controller(schemaStore);
    }

    public Response get(final Request request) throws DataStoreException {
        Response response = controller.get(request);
        response.setHeader("Content-Type", "application/schema+json");

        return response;
    }
}
