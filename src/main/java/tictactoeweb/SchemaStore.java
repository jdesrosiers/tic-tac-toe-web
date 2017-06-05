package tictactoeweb;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javaslang.control.Option;
import javaslang.control.Try;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javaslang.jackson.datatype.JavaslangModule;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

class SchemaStore {
    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaslangModule());

    private final Path storePath;
    private final JsonSchemaFactory factory;

    SchemaStore(Path storePath) {
        this.storePath = storePath;
        this.factory = JsonSchemaFactory.byDefault();
    }

    Try<JsonSchema> get(String schemaIdentifier) throws IOException {
        final InputStream in = Files.newInputStream(storePath.resolve("." + schemaIdentifier));
        return Try.of(() -> factory.getJsonSchema(mapper.readTree(in)));
    }

    Option<JsonNode> getAsJson(String schemaIdentifier) throws IOException, JsonProcessingException {
        return Option.of(storePath.resolve("." + schemaIdentifier))
            .filter(Files::exists)
            .map((path) -> Try.of(() -> Files.newInputStream(path)).get())
            .map((is) -> Try.of(() -> mapper.readTree(is)).get());
    }
}
