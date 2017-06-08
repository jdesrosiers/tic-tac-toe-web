package tictactoeweb.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;

import javaslang.collection.HashMap;
import javaslang.control.Option;

import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;
import org.flint.datastore.MapDataStore;
import org.flint.exception.NotFoundHttpException;
import org.flint.request.Method;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.response.Response;

public class SchemaControllerTest {

    private SchemaController schemaController;

    @Before
    public void setUp() {
        final DataStore schemaStore = new MapDataStore(HashMap.of("/schema/test.json", "{}"));
        this.schemaController = new SchemaController(schemaStore);
    }

    @Test
    public void itShouldSetTheContentTypeToApplicationSchemaJson() throws DataStoreException {
        final Request request = new Request(Method.GET, new OriginForm("/schema/test.json"));
        final Response response = schemaController.get(request);

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/schema+json")));
    }

    @Test(expected=NotFoundHttpException.class)
    public void itShould404IfTheSchemaDoesntExist() throws DataStoreException {
        final Request request = new Request(Method.GET, new OriginForm("/schema/notaschema.json"));
        schemaController.get(request);
    }

}
