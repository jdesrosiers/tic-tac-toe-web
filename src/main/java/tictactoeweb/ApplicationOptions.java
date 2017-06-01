package tictactoeweb;

import java.nio.file.Path;

class ApplicationOptions {
    private Path dataPath;
    private Path schemaPath;
    private Path webPath;
    private CorsOptions cors;

    private ApplicationOptions() { }

    public Path getDataPath() {
        return dataPath;
    }

    public Path getSchemaPath() {
        return schemaPath;
    }

    public Path getWebPath() {
        return webPath;
    }

    public CorsOptions getCors() {
        return cors;
    }

    static class Builder {
        private Path dataPath;
        private Path schemaPath;
        private Path webPath;
        private CorsOptions cors;

        public ApplicationOptions build() {
            ApplicationOptions options = new ApplicationOptions();
            options.dataPath = dataPath;
            options.schemaPath = schemaPath;
            options.webPath = webPath;
            options.cors = cors;

            return options;
        }

        public Builder dataPath(final Path path) {
            this.dataPath = path;
            return this;
        }

        public Builder schemaPath(final Path path) {
            this.schemaPath = path;
            return this;
        }

        public Builder webPath(final Path path) {
            this.webPath = path;
            return this;
        }

        public Builder cors(final CorsOptions options) {
            this.cors = options;
            return this;
        }
    }
}
