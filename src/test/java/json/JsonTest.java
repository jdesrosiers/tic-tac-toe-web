package json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.Test;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import scala.Symbol;

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
    public void itCanParseStringsAsScalaSymbols() throws IOException {
        FooSymbol fooSymbol = Json.parseAs("{\"foo\":\"bar\"}", FooSymbol.class);
        assertThat(fooSymbol.getFoo(), equalTo(Symbol.apply("bar")));
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
    public void itShouldStrignifyScalaSymbolsAsStrings() throws IOException {
        String json = "{\"foo\":\"bar\"}";
        FooSymbol fooSymbol = Json.parseAs(json, FooSymbol.class);
        assertThat(Json.stringify(fooSymbol), equalTo(json));
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

    private static class FooSymbol {
        private Symbol foo = Symbol.apply("bar");

        public Symbol getFoo() {
            return foo;
        }

        public void setFoo(Symbol foo) {
            this.foo = foo;
        }
    }

}
