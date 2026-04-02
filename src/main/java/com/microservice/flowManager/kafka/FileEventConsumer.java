package com.microservice.flowManager.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.flowManager.entity.FileRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.microservice.flowManager.repository.FileRecordRepository;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileEventConsumer {
    private final FileRecordRepository fileRecordRepository;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.result}")
    private String resultTopic;

    @KafkaListener(topics = "${kafka.topics.result}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleConversionResult(String message) {
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);

            Long fileId = Long.valueOf(event.get("fileId").toString());
            String status = event.get("status").toString();
            String convertedPath = event.getOrDefault("path", "").toString();

            FileRecord record = fileRecordRepository.findById(fileId)
                    .orElseThrow(() -> new RuntimeException("FileRecord not found: " + fileId));

            if ("SUCCESS".equals(status)) {
                record.setStatus(FileRecord.FileStatus.SUCCESS);
                record.setConvertedPath(convertedPath);
            } else {
                record.setStatus(FileRecord.FileStatus.ERROR);
            }

            record.setUpdatedAt(LocalDateTime.now());
            fileRecordRepository.save(record);

            log.info("Updated file {} status to {}", fileId, status);
        } catch (Exception e) {
            log.error("Error processing conversion result: {}", e.getMessage());
        }
    }
}
