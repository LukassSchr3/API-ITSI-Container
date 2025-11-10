package org.tgm.api.exception;

public class BackendException extends RuntimeException {
    public BackendException(String message) {
        super(message);
    }

    public BackendException(String message, Throwable cause) {
        super(message, cause);
    }
}

