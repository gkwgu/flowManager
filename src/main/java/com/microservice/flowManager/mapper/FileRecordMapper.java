package com.microservice.flowManager.mapper;

import com.microservice.flowManager.dto.FileStatusResponse;
import com.microservice.flowManager.dto.FileUploadEvent;
import com.microservice.flowManager.dto.UploadResponse;
import com.microservice.flowManager.entity.FileRecord;
import org.springframework.stereotype.Component;

@Component
public class FileRecordMapper {
    public FileStatusResponse toFileStatusResponse(FileRecord record) {
        return FileStatusResponse.builder()
                .fileId(record.getId())
                .status(record.getStatus())
                .originalPath(record.getOriginalPath())
                .convertedPath(record.getConvertedPath())
                .message("Current file status")
                .build();
    }

    public UploadResponse toUploadResponse(FileRecord record) {
        return UploadResponse.builder()
                .fileId(record.getId())
                .status(record.getStatus())
                .message("File uploaded and processing started")
                .build();
    }

    public FileUploadEvent toFileUploadEvent(FileRecord record, String bucket) {
        return FileUploadEvent.builder()
                .fileId(record.getId())
                .bucket(bucket)
                .path(record.getOriginalPath())
                .build();
    }
}
