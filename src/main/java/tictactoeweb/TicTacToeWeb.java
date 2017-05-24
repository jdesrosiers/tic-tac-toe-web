package tictactoeweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.cobspec.controller.FileSystemController;
import org.cobspec.controller.OptionsController;
import org.flint.Application;
import org.flint.response.Response;

class TicTacToeWeb {
    private static final int DEFAULT_PORT = 5000;

    public static void main(final String[] args) throws IOException {
        int port = Arguments.getPort(args).getOrElse(DEFAULT_PORT);
        build(Paths.get("web")).run(port);
    }

    static Application build(final Path web) {
        Application app = new Application();

        FileSystemController fileSystemController = new FileSystemController(web);
        app.get("*", fileSystemController::get);

        CorsMiddleware.Options options = new CorsMiddleware.Options();
        options.allowOrigin = "http://json-browser.s3-website-us-west-1.amazonaws.com";
        enableCors(app, options);

        return app;
    }

    private static Application enableCors(final Application app, final CorsMiddleware.Options options) {
        OptionsController optionsController = new OptionsController(app.getRouteMatcher());
        app.options("*", optionsController::options);

        CorsMiddleware corsMiddleware = new CorsMiddleware(options);
        app.after(corsMiddleware::cors);

        return app;
    }
}
