package tictactoeweb;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.flint.datastore.DataStore;
import org.flint.datastore.FileSystemDataStore;
import org.flint.controller.Controller;
import org.flint.controller.OptionsController;
import org.flint.Application;

import flint.cors.CorsOptions;
import flint.cors.CorsMiddleware;
import tictactoeweb.schema.SchemaStore;
import tictactoeweb.tictactoe.TicTacToeController;
import tictactoeweb.schema.SchemaController;
import tictactoeweb.datastore.FileSystemWithIndexDataStore;

public class TicTacToeWeb {
    private static final int DEFAULT_PORT = 5000;

    public static void main(final String[] args) throws IOException {
        final CorsOptions corsOptions = new CorsOptions.Builder()
            .allowOrigin("http://json-browser.s3-website-us-west-1.amazonaws.com")
            .exposeHeaders("Link,Location")
            .build();

        final TicTacToeWebOptions options = new TicTacToeWebOptions.Builder()
            .dataPath(Paths.get("."))
            .schemaPath(Paths.get("src/main"))
            .webPath(Paths.get("web"))
            .cors(corsOptions)
            .build();

        int port = Arguments.getPort(args).getOrElse(DEFAULT_PORT);
        build(options).run(port);
    }

    static Application build(final TicTacToeWebOptions options) {
        final Application app = new Application();

        ticTacToeApi(app, options.getDataPath(), options.getSchemaPath());
        enableCors(app, options.getCors());
        serveWeb(app, options.getWebPath());

        return app;
    }

    static Application ticTacToeApi(final Application app, final Path dataPath, final Path schemaPath) {
        final DataStore schemaStore = new FileSystemDataStore(schemaPath);
        final DataStore dataStore = new FileSystemDataStore(dataPath);
        final TicTacToeController ticTacToeController = new TicTacToeController(dataStore, schemaStore);
        app.get("/tictactoe", ticTacToeController::index);
        app.get("/tictactoe/*.json", ticTacToeController::get);
        app.post("/tictactoe/*.json", ticTacToeController::play);
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
        final DataStore dataStore = new FileSystemWithIndexDataStore(webPath);
        final Controller controller = new Controller(dataStore);
        app.get("*", controller::get);

        return app;
    }
}
