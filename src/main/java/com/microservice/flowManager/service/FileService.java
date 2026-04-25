package com.microservice.flowManager.service;

import com.microservice.flowManager.dto.FileStatusResponse;
import com.microservice.flowManager.dto.SubscriptionResponse;
import com.microservice.flowManager.dto.UploadResponse;
import com.microservice.flowManager.entity.FileRecord;
import com.microservice.flowManager.exceptions.FileNotFoundException;
import com.microservice.flowManager.exceptions.FileNotReadyException;
import com.microservice.flowManager.exceptions.FileRecordNotFoundException;
import com.microservice.flowManager.exceptions.FileSizeLimitException;
import com.microservice.flowManager.mapper.FileRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.microservice.flowManager.repository.FileRecordRepository;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRecordRepository fileRecordRepository;
    private final MinioService minioService;
    private final FileRecordService fileRecordService;
    private final FileRecordMapper fileRecordMapper;
    private final SubscriptionCacheService subscriptionCacheService;

    private static final long MAX_FREE_SIZE = 100L * 1024 * 1024; // 100 MB

    public UploadResponse uploadFile(MultipartFile file, String userLogin) throws Exception {
        SubscriptionResponse subscription = subscriptionCacheService.getSubscription(userLogin);
        boolean isPaid = "PAID".equals(subscription.getType()) && subscription.isActive();

        if (!isPaid && file.getSize() > MAX_FREE_SIZE) {
            throw new FileSizeLimitException(
                    "File size exceeds 100MB limit for free subscription. File size: "
                            + file.getSize() / (1024 * 1024) + "MB"
            );
        }

        String path = minioService.uploadFile(file);
        return fileRecordService.saveAndSendEvent(path);
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
