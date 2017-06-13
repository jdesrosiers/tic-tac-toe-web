package flint.cors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

import org.flint.controller.OptionsController;
import org.flint.request.Method;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;
import org.flint.Application;

@RunWith(DataProviderRunner.class)
public class CorsMiddlewareTest {

    public Application getApp(CorsOptions options) {
        Application app = new Application();

        app.get("/foo", request -> {
            Response response = Response.create();
            return response;
        });

        OptionsController optionsController = new OptionsController(app.getRouteMatcher());
        app.options("*", optionsController::options);

        CorsMiddleware corsMiddleware = new CorsMiddleware(options);
        app.after(corsMiddleware::cors);

        return app;
    }

    @Test
    public void itShouldIgnoreAnythingThatIsntACorsRequest() {
        CorsOptions options = new CorsOptions.Builder()
            .build();
        Application app = getApp(options);
        Request request = new Request(Method.OPTIONS, new OriginForm("/foo"));

        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Access-Control-Allow-Origin"), equalTo(Option.none()));
    }

    @Test
    public void itShouldHandlePreflight() {
        CorsOptions options = new CorsOptions.Builder()
            .allowOrigin("*")
            .maxAge("15")
            .build();
        Application app = getApp(options);

        Request request = new Request(Method.OPTIONS, new OriginForm("/foo"));
        request.setHeader("Origin", "http://www.foo.com");
        request.setHeader("Access-Control-Request-Method", "GET");
        request.setHeader("Access-Control-Request-Headers", "Location");

        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,HEAD,OPTIONS")));
        assertThat(response.getHeader("Access-Control-Allow-Methods"), equalTo(Option.of("GET")));
        assertThat(response.getHeader("Access-Control-Allow-Origin"), equalTo(Option.of("http://www.foo.com")));
        assertThat(response.getHeader("Access-Control-Allow-Headers"), equalTo(Option.of("Location")));
        assertThat(response.getHeader("Access-Control-Max-Age"), equalTo(Option.of("15")));
        assertThat(response.getHeader("Access-Control-Allow-Credentials"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Expose-Headers"), equalTo(Option.none()));
    }

    @Test
    public void itShouldIgnoreUnsupportedCorsPreFlightRequests() {
        CorsOptions options = new CorsOptions.Builder()
            .build();
        Application app = getApp(options);

        Request request = new Request(Method.OPTIONS, new OriginForm("/foo"));
        request.setHeader("Origin", "http://www.foo.com");
        request.setHeader("Access-Control-Request-Method", "POST");

        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,HEAD,OPTIONS")));
        assertThat(response.getHeader("Access-Control-Allow-Methods"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Origin"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Headers"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Max-Age"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Credentials"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Expose-Headers"), equalTo(Option.none()));
    }

    @DataProvider
    public static Object[][] dataProviderAllowOrigin() {
        return new Object[][] {
            { "*" },
            { "http://www.foo.com" },
            { "http://www.foo.com http://www.bar.com" },
            { "http://www.bar.com http://www.foo.com" }
        };
    }

    @Test
    @UseDataProvider("dataProviderAllowOrigin")
    public void itShouldEchoTheOriginInAllowOriginIfOriginIsAllowed(String origin) {
        CorsOptions options = new CorsOptions.Builder()
            .allowOrigin(origin)
            .build();
        Application app = getApp(options);

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        request.setHeader("Origin", "http://www.foo.com");

        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Access-Control-Allow-Methods"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Origin"), equalTo(Option.of("http://www.foo.com")));
        assertThat(response.getHeader("Access-Control-Allow-Headers"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Max-Age"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Credentials"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Expose-Headers"), equalTo(Option.none()));
    }

    @Test
    public void itShouldHaveNullInAllowOriginIfOriginIsNotAllowed() {
        CorsOptions options = new CorsOptions.Builder()
            .allowOrigin("http://www.bar.com")
            .maxAge("15")
            .build();
        Application app = getApp(options);

        Request request = new Request(Method.OPTIONS, new OriginForm("/foo"));
        request.setHeader("Origin", "http://www.foo.com");
        request.setHeader("Access-Control-Request-Method", "GET");

        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,HEAD,OPTIONS")));
        assertThat(response.getHeader("Access-Control-Allow-Methods"), equalTo(Option.of("GET")));
        assertThat(response.getHeader("Access-Control-Allow-Origin"), equalTo(Option.of("null")));
        assertThat(response.getHeader("Access-Control-Allow-Headers"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Max-Age"), equalTo(Option.of("15")));
        assertThat(response.getHeader("Access-Control-Allow-Credentials"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Expose-Headers"), equalTo(Option.none()));
    }

    @DataProvider
    public static Object[][] dataProviderAllowMethodsOption() {
        return new Object[][] {
            { "GET" },
            { "GET,POST" }
        };
    }

    @Test
    @UseDataProvider("dataProviderAllowMethodsOption")
    public void itShouldAllowMethodsInTheAllowMethodsOption(String allowMethods) {
        CorsOptions options = new CorsOptions.Builder()
            .allowOrigin("http://www.foo.com")
            .allowMethods(allowMethods)
            .maxAge("15")
            .build();
        Application app = getApp(options);

        Request request = new Request(Method.OPTIONS, new OriginForm("/foo"));
        request.setHeader("Origin", "http://www.foo.com");
        request.setHeader("Access-Control-Request-Method", "GET");

        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,HEAD,OPTIONS")));
        assertThat(response.getHeader("Access-Control-Allow-Methods"), equalTo(Option.of("GET")));
        assertThat(response.getHeader("Access-Control-Allow-Origin"), equalTo(Option.of("http://www.foo.com")));
        assertThat(response.getHeader("Access-Control-Allow-Headers"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Max-Age"), equalTo(Option.of("15")));
        assertThat(response.getHeader("Access-Control-Allow-Credentials"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Expose-Headers"), equalTo(Option.none()));
    }

    @Test
    public void itShouldNotAllowMethodsNotInTheAllowMethodsOption() {
        CorsOptions options = new CorsOptions.Builder()
            .allowOrigin("http://www.bar.com")
            .allowMethods("GET")
            .maxAge("15")
            .build();
        Application app = getApp(options);

        Request request = new Request(Method.OPTIONS, new OriginForm("/foo"));
        request.setHeader("Origin", "http://www.foo.com");
        request.setHeader("Access-Control-Request-Method", "POST");

        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,HEAD,OPTIONS")));
        assertThat(response.getHeader("Access-Control-Allow-Methods"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Origin"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Headers"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Max-Age"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Credentials"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Expose-Headers"), equalTo(Option.none()));
    }

    @Test
    public void itShouldIgnoreIfHeadersThatArentAllowedAreRequested() {
        CorsOptions options = new CorsOptions.Builder()
            .allowHeaders("")
            .build();
        Application app = getApp(options);

        Request request = new Request(Method.OPTIONS, new OriginForm("/foo"));
        request.setHeader("Origin", "http://www.foo.com");
        request.setHeader("Access-Control-Request-Method", "GET");
        request.setHeader("Access-Control-Request-Headers", "If-Modified-Since");

        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,HEAD,OPTIONS")));
        assertThat(response.getHeader("Access-Control-Allow-Methods"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Origin"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Headers"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Max-Age"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Credentials"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Expose-Headers"), equalTo(Option.none()));
    }

    @Test
    public void itShouldSupportExposingHeaders() {
        CorsOptions options = new CorsOptions.Builder()
            .allowOrigin("http://www.foo.com")
            .allowCredentials(true)
            .exposeHeaders("Foo-Bar,Baz")
            .build();
        Application app = getApp(options);

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        request.setHeader("Origin", "http://www.foo.com");

        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Access-Control-Allow-Methods"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Origin"), equalTo(Option.of("http://www.foo.com")));
        assertThat(response.getHeader("Access-Control-Allow-Headers"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Max-Age"), equalTo(Option.none()));
        assertThat(response.getHeader("Access-Control-Allow-Credentials"), equalTo(Option.of("true")));
        assertThat(response.getHeader("Access-Control-Expose-Headers"), equalTo(Option.of("Foo-Bar,Baz")));
    }

}
