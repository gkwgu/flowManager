package com.microservice.flowManager.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.flowManager.dto.FileUploadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.upload}")
    private String uploadTopic;

    public void sendFileEvent(FileUploadEvent event) throws Exception {
        String message = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(uploadTopic, message);
        log.info("Sent file event to Kafka: {}", message);
    }
}
