package com.microservice.flowManager.service;

import com.microservice.flowManager.dto.UploadResponse;
import com.microservice.flowManager.entity.FileRecord;
import com.microservice.flowManager.kafka.FileEventProducer;
import com.microservice.flowManager.mapper.FileRecordMapper;
import com.microservice.flowManager.repository.FileRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileRecordService {

    private final FileRecordRepository fileRecordRepository;
    private final FileEventProducer fileEventProducer;
    private final FileRecordMapper fileRecordMapper;

    @Value("${minio.bucket}")
    private String bucket;

    @Transactional
    public UploadResponse saveAndSendEvent(String path) throws Exception {
        FileRecord record = FileRecord.builder()
                .originalPath(path)
                .status(FileRecord.FileStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        record = fileRecordRepository.save(record);
        fileEventProducer.sendFileEvent(fileRecordMapper.toFileUploadEvent(record, bucket));
        return fileRecordMapper.toUploadResponse(record);
    }
}