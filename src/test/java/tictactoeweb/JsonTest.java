package tictactoeweb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.Test;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javaslang.collection.List;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonTest {

    @Test
    public void itShouldParseAStringAsAJsonNode() throws IOException {
        JsonNode node = Json.parse("{\"foo\":\"bar\"}");
        assertThat(node.get("foo").textValue(), equalTo("bar"));
    }

    @Test
    public void itShouldParseAnInputStreamAsAJsonNode() throws IOException {
        InputStream is = new ByteArrayInputStream("{\"foo\":\"bar\"}".getBytes());
        JsonNode node = Json.parse(is);
        assertThat(node.get("foo").textValue(), equalTo("bar"));
    }

    @Test
    public void itShouldParseAStringAsAnObject() throws IOException {
        Foo foo = Json.parseAs("{\"foo\":\"bar\"}", Foo.class);
        assertThat(foo.getFoo(), equalTo("bar"));
    }

    @Test
    public void itShouldParseJavaslangStructures() throws IOException {
        Foos foos = Json.parseAs("{\"foos\":[\"bar\"]}", Foos.class);
        assertThat(foos.getFoos(), equalTo(List.of("bar")));
    }

    @Test
    public void itShouldParseAnInputStreamAsAnObject() throws IOException {
        InputStream is = new ByteArrayInputStream("{\"foo\":\"bar\"}".getBytes());
        Foo foo = Json.parseAs(is, Foo.class);
        assertThat(foo.getFoo(), equalTo("bar"));
    }

    @Test
    public void itShouldStrignifyAJsonNode() throws IOException {
        String json = "{\"foo\":\"bar\"}";
        JsonNode node = Json.parse(json);
        assertThat(Json.stringify(node), equalTo(json));
    }

    @Test
    public void itShouldStrignifyAnObject() throws IOException {
        String json = "{\"foo\":\"bar\"}";
        Foo foo = Json.parseAs(json, Foo.class);
        assertThat(Json.stringify(foo), equalTo(json));
    }

    @Test
    public void itShouldStrignifyJavaslangStructures() throws IOException {
        String json = "{\"foos\":[\"bar\"]}";
        Foos foos = Json.parseAs(json, Foos.class);
        assertThat(Json.stringify(foos), equalTo(json));
    }

    private static class Foo {
        private String foo = "bar";

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }
    }

    private static class Foos {
        private List<String> foos;

        public List<String> getFoos() {
            return foos;
        }

        public void setFoos(List<String> foos) {
            this.foos = foos;
        }
    }

}
