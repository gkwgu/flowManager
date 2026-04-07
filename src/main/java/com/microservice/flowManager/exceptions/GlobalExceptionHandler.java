package com.microservice.flowManager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFound(FileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(Map.of(
                "error", e.getMessage()
        ));
    }
    @ExceptionHandler(FileRecordNotFoundException.class)
    public ResponseEntity<?> handleFileRecordNotFound(FileRecordNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(FileNotReadyException.class)
    public ResponseEntity<?> handleFileNotReady(FileNotReadyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", e.getMessage()
        ));
    }
}
