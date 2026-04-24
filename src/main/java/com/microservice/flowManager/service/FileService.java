package com.microservice.flowManager.service;

import com.microservice.flowManager.dto.FileStatusResponse;
import com.microservice.flowManager.dto.SubscriptionResponse;
import com.microservice.flowManager.dto.UploadResponse;
import com.microservice.flowManager.entity.FileRecord;
import com.microservice.flowManager.exceptions.FileNotFoundException;
import com.microservice.flowManager.exceptions.FileNotReadyException;
import com.microservice.flowManager.exceptions.FileRecordNotFoundException;
import com.microservice.flowManager.exceptions.FileSizeLimitException;
import com.microservice.flowManager.kafka.FileEventProducer;
import com.microservice.flowManager.mapper.FileRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.microservice.flowManager.repository.FileRecordRepository;
import com.microservice.flowManager.client.SubscriptionClient;

import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRecordRepository fileRecordRepository;
    private final MinioService minioService;
    private final FileEventProducer fileEventProducer;
    private final FileRecordMapper fileRecordMapper;
    private final SubscriptionClient subscriptionClient;

    @Value("${minio.bucket}")
    private String bucket;

    private static final long MAX_FREE_SIZE = 100L * 1024 * 1024; // 100 MB

    @Transactional
    public UploadResponse uploadFile(MultipartFile file, String userLogin) throws Exception {

        SubscriptionResponse subscription = subscriptionClient.getSubscription(userLogin);

        boolean isPaid = "PAID".equals(subscription.getType()) && subscription.isActive();

        if (!isPaid && file.getSize() > MAX_FREE_SIZE) {
            throw new FileSizeLimitException(
                    "File size exceeds 100MB limit for free subscription. File size: "
                            + file.getSize() / (1024 * 1024) + "MB"
            );
        }

        String path = minioService.uploadFile(file);

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

    public FileStatusResponse getStatus(Long id) {
        FileRecord record = fileRecordRepository.findById(id)
                .orElseThrow(() -> new FileRecordNotFoundException(id));
        return fileRecordMapper.toFileStatusResponse(record);
    }

    public InputStream getConvertedFile(Long id) throws Exception {
        FileRecord record = fileRecordRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id));

        if (record.getStatus() != FileRecord.FileStatus.SUCCESS) {
            throw new FileNotReadyException(id, record.getStatus());
        }

        return minioService.downloadFile(record.getConvertedPath());
    }
}
