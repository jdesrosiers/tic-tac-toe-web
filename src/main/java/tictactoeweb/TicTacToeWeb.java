package tictactoeweb;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.cobspec.controller.FileSystemController;
import org.cobspec.controller.OptionsController;
import org.flint.Application;
import org.flint.response.Response;

public class TicTacToeWeb {
    private static final int DEFAULT_PORT = 5000;

    public static void main(String[] args) throws IOException {
        Options options = new Options();
        options.dataPath = Paths.get(".");
        options.schemaPath = Paths.get("src/main");
        options.webPath = Paths.get("web");
        options.cors.allowOrigin = "http://json-browser.s3-website-us-west-1.amazonaws.com";
        options.cors.exposeHeaders = "Link,Location";

        int port = Arguments.getPort(args).getOrElse(DEFAULT_PORT);
        build(options).run(port);
    }

    static class Options {
        Path dataPath;
        Path schemaPath;
        Path webPath;
        final CorsMiddleware.Options cors = new CorsMiddleware.Options();
    }

    static Application build(Options options) {
        final Application app = new Application();

        ticTacToeApi(app, options.dataPath, options.schemaPath);
        enableCors(app, options.cors);
        serveWeb(app, options.webPath);

        return app;
    }

    static Application ticTacToeApi(final Application app, final Path dataPath, final Path schemaPath) {
        final SchemaStore schemaStore = new SchemaStore(schemaPath);
        final TicTacToeController ticTacToeController = new TicTacToeController(dataPath, schemaStore);
        app.get("/tictactoe", ticTacToeController::index);
        app.get("/tictactoe/*.json", ticTacToeController::get);
        app.post("/tictactoe", ticTacToeController::create);

        final SchemaController schemaController = new SchemaController(schemaStore);
        app.get("/schema/*.json", schemaController::get);

        return app;
    }

    static Application enableCors(final Application app, final CorsMiddleware.Options options) {
        final OptionsController optionsController = new OptionsController(app.getRouteMatcher());
        app.options("*", optionsController::options);

        final CorsMiddleware corsMiddleware = new CorsMiddleware(options);
        app.after(corsMiddleware::cors);

        return app;
    }

    static Application serveWeb(final Application app, final Path webPath) {
        final FileSystemController fileSystemController = new FileSystemController(webPath);
        app.get("*", fileSystemController::get);

        return app;
    }
}
