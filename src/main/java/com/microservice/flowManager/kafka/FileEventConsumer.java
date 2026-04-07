package com.microservice.flowManager.kafka;

import com.microservice.flowManager.dto.ConversionResultEvent;
import com.microservice.flowManager.dto.ConversionStatus;
import com.microservice.flowManager.entity.FileRecord;
import com.microservice.flowManager.exceptions.FileRecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.microservice.flowManager.repository.FileRecordRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileEventConsumer {

    private final FileRecordRepository fileRecordRepository;

    @KafkaListener(topics = "${kafka.topics.result}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleConversionResult(ConversionResultEvent event) {
        log.info("Received conversion result: {}", event);

        FileRecord record = fileRecordRepository.findById(event.getFileId())
                .orElseThrow(() -> new FileRecordNotFoundException(event.getFileId()));

        if (ConversionStatus.SUCCESS.equals(event.getStatus())) {
            record.setStatus(FileRecord.FileStatus.SUCCESS);
            record.setConvertedPath(event.getPath());
        } else {
            record.setStatus(FileRecord.FileStatus.ERROR);
        }

        record.setUpdatedAt(LocalDateTime.now());
        fileRecordRepository.save(record);

        log.info("Updated file {} status to {}", event.getFileId(), event.getStatus());
    }
}
