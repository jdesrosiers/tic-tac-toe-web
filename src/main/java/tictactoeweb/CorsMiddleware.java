package tictactoeweb;

import java.util.Arrays;

import javaslang.collection.List;
import javaslang.control.Option;

import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.response.Response;

class CorsMiddleware {
    private final CorsOptions options;

    CorsMiddleware(final CorsOptions options) {
        this.options = options;
    }

    Response cors(final Request request, final Response response) {
        return Option.of(response)
            .filter(r -> isCorsRequest(request))
            .flatMap(r -> handleCorsRequest(request, r))
            .map(r -> setAllowOrigin(request, r))
            .map(r -> setAllowCredentials(request, r))
            .getOrElse(response);
    }

    private boolean isCorsRequest(final Request request) {
        return request.getHeader("Origin").isDefined();
    }

    private Option<Response> handleCorsRequest(final Request request, final Response response) {
        return isPreflightRequest(request)
            ? handlePreflightRequest(request, response)
            : handleActualRequest(request, response);
    }

    private boolean isPreflightRequest(final Request request) {
        return request.getMethod().equals(Method.OPTIONS)
            && request.getHeader("Access-Control-Request-Method").isDefined();
    }

    private Option<Response> handlePreflightRequest(final Request request, final Response response) {
        return Option.of(response)
            .filter(r -> isMethodAllowed(request, r))
            .filter(r -> areHeadersAllowed(request, r))
            .map(r -> setAllowHeaders(request, r))
            .map(r -> setAllowMethods(request, r))
            .map(r -> setMaxAge(request, r));
    }

    private Option<Response> handleActualRequest(final Request request, final Response response) {
        return Option.of(response)
            .map(r -> setExposeHeaders(request, r));
    }

    private boolean isMethodAllowed(final Request request, final Response response) {
        String allowMethods = Option.of(options.getAllowMethods())
            .getOrElse(() -> response.getHeader("Allow").get());

        return request.getHeader("Access-Control-Request-Method")
            .filter(explode(allowMethods, ",")::contains)
            .isDefined();
    }

    private boolean areHeadersAllowed(final Request request, final Response response) {
        String requestHeaders = request.getHeader("Access-Control-Request-Headers").getOrElse("");
        String allowedHeaders = Option.of(options.getAllowHeaders())
            .getOrElse(requestHeaders);

        return explode(allowedHeaders, ",")
            .containsAll(explode(requestHeaders, ","));
    }

    private Response setAllowOrigin(final Request request, final Response response) {
        String origin = request.getHeader("Origin").get();

        String allowOrigin;
        if ("*".equals(options.getAllowOrigin())) {
            allowOrigin = origin;
        } else {
            allowOrigin = Option.of(options.getAllowOrigin())
                .map(origins -> explode(origins, " "))
                .getOrElse(List::empty)
                .find(host -> host.equals(origin))
                .getOrElse("null");
        }

        response.setHeader("Access-Control-Allow-Origin", allowOrigin);

        return response;
    }

    private Response setAllowHeaders(final Request request, final Response response) {
        request.getHeader("Access-Control-Request-Headers")
            .peek(headers -> response.setHeader("Access-Control-Allow-Headers", headers));

        return response;
    }

    private Response setAllowMethods(final Request request, final Response response) {
        request.getHeader("Access-Control-Request-Method")
            .peek(methods -> response.setHeader("Access-Control-Allow-Methods", methods));

        return response;
    }

    private Response setMaxAge(final Request request, final Response response) {
        Option.of(options.getMaxAge())
            .peek(maxAge -> response.setHeader("Access-Control-Max-Age", maxAge));

        return response;
    }

    private Response setExposeHeaders(final Request request, final Response response) {
        Option.of(options.getExposeHeaders())
            .peek(exposeHeaders -> response.setHeader("Access-Control-Expose-Headers", exposeHeaders));

        return response;
    }

    private Response setAllowCredentials(final Request request, final Response response) {
        Option.of(options.getAllowCredentials())
            .peek(allowCredentials -> response.setHeader("Access-Control-Allow-Credentials", "true"));

        return response;
    }

    private List<String> explode(final String subject, final String delimiter) {
        return List.ofAll(Arrays.asList(subject.split(delimiter)));
    }
}
