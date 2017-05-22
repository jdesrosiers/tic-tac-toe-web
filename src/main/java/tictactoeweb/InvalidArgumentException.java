package tictactoeweb;

class InvalidArgumentException extends RuntimeException {
    InvalidArgumentException() {
        super();
    }

    InvalidArgumentException(String message) {
        super(message);
    }

    InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    protected	InvalidArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    InvalidArgumentException(Throwable cause) {
        super(cause);
    }
}

