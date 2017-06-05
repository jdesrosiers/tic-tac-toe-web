package tictactoeweb.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;

@RunWith(DataProviderRunner.class)
public class SchemaStoreTest {

    @DataProvider
    public static Object[][] dataProviderSchemas() {
        return new Object[][] {
            { "/schema/test.json", true },
            { "/schema/foo.json", false }
        };
    }

    @Test
    @UseDataProvider("dataProviderSchemas")
    public void itShouldTestIfASchemaExists(String schema, boolean exists) {
        SchemaStore schemaStore = new SchemaStore(Paths.get("src/test/resources"));
        assertThat(schemaStore.hasSchema("/schema/test.json"), equalTo(true));
    }

    @Test
    public void itShouldGetASchemaAsAJsonSchema() throws IOException, ProcessingException {
        SchemaStore schemaStore = new SchemaStore(Paths.get("src/test/resources"));
        assertThat(schemaStore.getSchema("/schema/test.json"), instanceOf(JsonSchema.class));
    }

    @Test
    public void itShouldGetASchemaAsAJsonNode() throws IOException {
        SchemaStore schemaStore = new SchemaStore(Paths.get("src/test/resources"));
        assertThat(schemaStore.getJson("/schema/test.json"), instanceOf(JsonNode.class));
    }

    @Test
    public void itShouldGetASchemaAsAString() throws IOException {
        SchemaStore schemaStore = new SchemaStore(Paths.get("src/test/resources"));
        assertThat(schemaStore.getString("/schema/test.json"), instanceOf(String.class));
    }

}
