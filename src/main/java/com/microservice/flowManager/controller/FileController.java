package com.microservice.flowManager.controller;

import com.microservice.flowManager.dto.FileStatusResponse;
import com.microservice.flowManager.dto.UploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.microservice.flowManager.service.FileService;

import java.io.InputStream;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public UploadResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-User-Login") String userLogin) throws Exception {
        return fileService.uploadFile(file, userLogin);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<FileStatusResponse> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getStatus(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) throws Exception {
        InputStream stream = fileService.getConvertedFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(stream));
    }
}
