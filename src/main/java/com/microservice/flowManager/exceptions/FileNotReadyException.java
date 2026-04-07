package com.microservice.flowManager.exceptions;

import com.microservice.flowManager.entity.FileRecord;

public class FileNotReadyException extends RuntimeException {
    public FileNotReadyException(Long id, FileRecord.FileStatus status) {
        super("File " + id + " is not ready. Current status: " + status);
    }
}
