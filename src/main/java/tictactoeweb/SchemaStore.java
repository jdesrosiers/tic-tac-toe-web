package tictactoeweb;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.JsonNode;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

class SchemaStore {
    private final Path storePath;
    private final JsonSchemaFactory factory;

    SchemaStore(Path storePath) {
        this.storePath = storePath;
        this.factory = JsonSchemaFactory.byDefault();
    }

    public boolean hasSchema(String schemaIdentifier) {
        return Files.exists(getPath(schemaIdentifier));
    }

    public JsonSchema getSchema(String schemaIdentifier) throws IOException, ProcessingException {
        final InputStream is = Files.newInputStream(getPath(schemaIdentifier));
        return factory.getJsonSchema(Json.parse(is));
    }

    public JsonNode getJson(String schemaIdentifier) throws IOException {
        final InputStream is = Files.newInputStream(getPath(schemaIdentifier));
        return Json.parse(is);
    }

    public String getString(String schemaIdentifier) throws IOException {
        return new String(Files.readAllBytes(getPath(schemaIdentifier)));
    }

    private Path getPath(String schemaIdentifier) {
        return storePath.resolve("." + schemaIdentifier);
    }
}
