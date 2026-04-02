package com.microservice.flowManager.service;

import com.microservice.flowManager.entity.FileRecord;
import com.microservice.flowManager.kafka.FileEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.microservice.flowManager.repository.FileRecordRepository;

import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRecordRepository fileRecordRepository;
    private final MinioService minioService;
    private final FileEventProducer fileEventProducer;

    @Value("${minio.bucket}")
    private String bucket;

    public FileRecord uploadFile(MultipartFile file) throws Exception {
        String path = minioService.uploadFile(file);

        FileRecord record = FileRecord.builder()
                .originalPath(path)
                .status(FileRecord.FileStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        record = fileRecordRepository.save(record);

        fileEventProducer.sendFileEvent(record.getId(), bucket, path);

        return record;
    }

    public FileRecord getStatus(Long id) {
        return fileRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found: " + id));
    }

    public InputStream getConvertedFile(Long id) throws Exception {
        FileRecord record = getStatus(id);

        if (record.getStatus() != FileRecord.FileStatus.SUCCESS) {
            throw new RuntimeException("File not ready. Status: " + record.getStatus());
        }

        return minioService.downloadFile(record.getConvertedPath());
    }
}
