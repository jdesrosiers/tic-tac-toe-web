package json;

import java.io.InputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javaslang.jackson.datatype.JavaslangModule;

public class Json {
    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaslangModule());

    public static JsonNode parse(String subject) throws IOException {
        return mapper.readTree(subject);
    }

    public static JsonNode parse(InputStream subject) throws IOException {
        return mapper.readTree(subject);
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
}
