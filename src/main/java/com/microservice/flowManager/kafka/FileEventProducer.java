package com.microservice.flowManager.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.upload}")
    private String uploadTopic;

    public void sendFileEvent(Long fileId, String bucket, String path) throws Exception {
        Map<String, Object> event = Map.of(
                "fileId", fileId,
                "bucket", bucket,
                "path", path
        );
        String message = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(uploadTopic, message);
        log.info("Sent file event to Kafka: {}", message);
    }
}
