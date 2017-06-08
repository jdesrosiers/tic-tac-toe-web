package tictactoeweb.schema;

import java.io.InputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;

import json.Json;

public class SchemaStore {
    private DataStore dataStore;
    private final JsonSchemaFactory factory;

    public SchemaStore(DataStore dataStore) {
        this.dataStore = dataStore;
        this.factory = JsonSchemaFactory.byDefault();
    }

    public JsonSchema fetchSchema(String identifier) throws DataStoreException, ProcessingException {
        final InputStream is = dataStore.fetch(identifier);
        try {
            return factory.getJsonSchema(Json.parse(is));
        } catch (IOException ioe) {
            throw new DataStoreException(ioe);
        }
    }

    public JsonNode fetchJson(String identifier) throws DataStoreException {
        final InputStream is = dataStore.fetch(identifier);
        try {
            return Json.parse(is);
        } catch (IOException ioe) {
            throw new DataStoreException(ioe);
        }
    }
}
