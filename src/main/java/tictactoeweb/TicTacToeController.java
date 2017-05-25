package tictactoeweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import javaslang.jackson.datatype.JavaslangModule;
import javaslang.control.Try;

import org.cobspec.controller.FileSystemController;
import org.flint.exception.BadRequestHttpException;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import tictactoeweb.model.CreateGame;

class TicTacToeController {
    final private static ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaslangModule());

    final private FileSystemController fileSystemController;
    final private JsonSchema createSchema;

    public TicTacToeController(final Path path) throws IOException, ProcessingException {
        this.fileSystemController = new FileSystemController(path);
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        final JsonNode createSchemaJson = mapper.readTree(Files.newInputStream(Paths.get("src/main/schema/create.json")));
        this.createSchema = factory.getJsonSchema(createSchemaJson);
    }

    public Response index(final Request request) throws IOException {
        final Response response = fileSystemController.get(request);
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Link", "</schema/index.json>; rel=describedby");

        return response;
    }

    public Response create(final Request request) throws IOException {
        Try.of(() -> createSchema.validate(mapper.readTree(request.getBody())))
            .filter(ProcessingReport::isSuccess)
            .getOrElseThrow(() -> new BadRequestHttpException());

        final CreateGame create = mapper.readValue(request.getBody(), CreateGame.class);

        final String json = mapper.writer().writeValueAsString(create.toGame());
        Response response = fileSystemController.write(request, Paths.get(request.getPath() + "/1.json"), json);
        response.setStatusCode(StatusCode.SEE_OTHER);

        return response;
    }

    public Response get(final Request request) throws IOException {
        final Response response = fileSystemController.get(request);
        response.setHeader("Content-Type", "application/json");

        return response;
    }
}
