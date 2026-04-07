package com.microservice.flowManager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_records")
@Getter
@Setter
@Builder
public class FileRecord {

    @Id
    @GeneratedValue
    private Long id;

    private String originalPath;

    private String convertedPath;

    @Enumerated(EnumType.STRING)
    private FileStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum FileStatus{
        PROCESSING, SUCCESS, ERROR
    }
}
