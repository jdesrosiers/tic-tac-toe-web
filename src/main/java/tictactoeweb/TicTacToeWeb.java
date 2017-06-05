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

    public static void main(final String[] args) throws IOException {
        final CorsOptions corsOptions = new CorsOptions.Builder()
            .allowOrigin("http://json-browser.s3-website-us-west-1.amazonaws.com")
            .exposeHeaders("Link,Location")
            .build();

        final ApplicationOptions options = new ApplicationOptions.Builder()
            .dataPath(Paths.get("."))
            .schemaPath(Paths.get("src/main"))
            .webPath(Paths.get("web"))
            .cors(corsOptions)
            .build();

        int port = Arguments.getPort(args).getOrElse(DEFAULT_PORT);
        build(options).run(port);
    }

    static Application build(final ApplicationOptions options) {
        final Application app = new Application();

        ticTacToeApi(app, options.getDataPath(), options.getSchemaPath());
        enableCors(app, options.getCors());
        serveWeb(app, options.getWebPath());

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

    static Application enableCors(final Application app, final CorsOptions options) {
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
