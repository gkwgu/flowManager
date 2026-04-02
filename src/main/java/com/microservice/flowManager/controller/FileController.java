package com.microservice.flowManager.controller;

import com.microservice.flowManager.dto.FileStatusResponse;
import com.microservice.flowManager.entity.FileRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.microservice.flowManager.service.FileService;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileRecord record = fileService.uploadFile(file);
            return ResponseEntity.ok(Map.of(
                    "fileId", record.getId(),
                    "status", record.getStatus(),
                    "message", "File uploaded and processing started"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<FileStatusResponse> getStatus(@PathVariable Long id) {
        FileRecord record = fileService.getStatus(id);

        return ResponseEntity.ok(FileStatusResponse.builder()
                .fileId(record.getId())
                .status(record.getStatus())
                .originalPath(record.getOriginalPath())
                .convertedPath(record.getConvertedPath())
                .message("Current file status")
                .build());
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) {
        try {
            InputStream stream = fileService.getConvertedFile(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(stream));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
