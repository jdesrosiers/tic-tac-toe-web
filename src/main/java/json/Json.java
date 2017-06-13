package json;

import java.io.InputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.module.scala.DefaultScalaModule;

import scala.Symbol;

public class Json {
    private static final SimpleModule symbolModule = new SimpleModule()
        .addSerializer(Symbol.class, new SymbolSerializer())
        .addDeserializer(Symbol.class, new SymbolDeserializer());

    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new DefaultScalaModule())
        .registerModule(symbolModule);

    public static JsonNode parse(String subject) throws IOException {
        return mapper.readTree(subject);
    }

    public static JsonNode parse(InputStream subject) throws IOException {
        return mapper.readTree(subject);
    }

    public static <T> T parseAs(JsonNode json, Class<T> type) throws IOException {
        return mapper.treeToValue(json, type);
    }

    public static <T> T parseAs(String json, Class<T> type) throws IOException {
        return mapper.readValue(json, type);
    }

    public static <T> T parseAs(InputStream jsonStream, Class<T> type) throws IOException {
        return mapper.readValue(jsonStream, type);
    }

    public static <T> String stringify(T subject) throws JsonProcessingException {
        return mapper.writer().writeValueAsString(subject);
    }

    private static class SymbolSerializer extends StdSerializer<Symbol> {
        public SymbolSerializer() {
            this(null);
        }

        public SymbolSerializer(Class<Symbol> t) {
            super(t);
        }

        @Override
        public void serialize(Symbol value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeString(value.name());
        }
    }

    private static class SymbolDeserializer extends StdDeserializer<Symbol> {
        public SymbolDeserializer() {
            this(null);
        }

        public SymbolDeserializer(Class<?> t) {
            super(t);
        }

        @Override
        public Symbol deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = jp.getCodec().readTree(jp);
            return Symbol.apply(node.textValue());
        }
    }
}
