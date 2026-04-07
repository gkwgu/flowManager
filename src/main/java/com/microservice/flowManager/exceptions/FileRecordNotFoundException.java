package com.microservice.flowManager.exceptions;

public class FileRecordNotFoundException extends RuntimeException {
    public FileRecordNotFoundException(Long id) {
        super("File record not found with id: " + id);
    }
}
