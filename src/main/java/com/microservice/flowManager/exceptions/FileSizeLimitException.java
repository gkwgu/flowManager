package com.microservice.flowManager.exceptions;

public class FileSizeLimitException extends RuntimeException {
    public FileSizeLimitException(String message) {
        super(message);
    }
}
