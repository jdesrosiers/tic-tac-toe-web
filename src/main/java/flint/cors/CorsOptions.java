package flint.cors;

public class CorsOptions {
    private String allowMethods;
    private String allowHeaders;
    private String maxAge;
    private String allowOrigin;
    private Boolean allowCredentials;
    private String exposeHeaders;

    private CorsOptions() { }

    public String getAllowMethods() {
        return allowMethods;
    }

    public String getAllowHeaders() {
        return allowHeaders;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public String getAllowOrigin() {
        return allowOrigin;
    }

    public Boolean getAllowCredentials() {
        return allowCredentials;
    }

    public String getExposeHeaders() {
        return exposeHeaders;
    }

    public static class Builder {
        private String allowMethods;
        private String allowHeaders;
        private String maxAge;
        private String allowOrigin;
        private Boolean allowCredentials;
        private String exposeHeaders;

        public CorsOptions build() {
            CorsOptions options = new CorsOptions();
            options.allowMethods = allowMethods;
            options.allowHeaders = allowHeaders;
            options.maxAge = maxAge;
            options.allowOrigin = allowOrigin;
            options.allowCredentials = allowCredentials;
            options.exposeHeaders = exposeHeaders;

            return options;
        }

        public Builder allowMethods(final String allowMethods) {
            this.allowMethods = allowMethods;
            return this;
        }

        public Builder allowHeaders(final String allowHeaders) {
            this.allowHeaders = allowHeaders;
            return this;
        }

        public Builder maxAge(final String maxAge) {
            this.maxAge = maxAge;
            return this;
        }

        public Builder allowOrigin(final String allowOrigin) {
            this.allowOrigin = allowOrigin;
            return this;
        }

        public Builder allowCredentials(final Boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
            return this;
        }

        public Builder exposeHeaders(final String exposeHeaders) {
            this.exposeHeaders = exposeHeaders;
            return this;
        }
    }
}
