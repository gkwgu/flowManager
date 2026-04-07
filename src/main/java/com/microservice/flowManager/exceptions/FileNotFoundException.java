package com.microservice.flowManager.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(Long id) {
        super("File not found with id: " + id);
    }
}
