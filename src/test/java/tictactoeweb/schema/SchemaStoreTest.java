package tictactoeweb.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.Test;

import javaslang.collection.HashMap;
import javaslang.Tuple;

import com.fasterxml.jackson.databind.JsonNode;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;

import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;
import org.flint.datastore.MapDataStore;

public class SchemaStoreTest {

    @Test
    public void itShouldGetASchemaAsAJsonSchema() throws DataStoreException, ProcessingException {
        final DataStore dataStore = new MapDataStore(HashMap.ofEntries(Tuple.of("/schema/test.json", "{}")));
        final SchemaStore schemaStore = new SchemaStore(dataStore);
        assertThat(schemaStore.fetchSchema("/schema/test.json"), instanceOf(JsonSchema.class));
    }

    @Test
    public void itShouldGetASchemaAsAJsonNode() throws DataStoreException {
        final DataStore dataStore = new MapDataStore(HashMap.ofEntries(Tuple.of("/schema/test.json", "{}")));
        final SchemaStore schemaStore = new SchemaStore(dataStore);
        assertThat(schemaStore.fetchJson("/schema/test.json"), instanceOf(JsonNode.class));
    }

}
