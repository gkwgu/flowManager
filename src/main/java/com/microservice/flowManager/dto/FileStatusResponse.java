package com.microservice.flowManager.dto;

import com.microservice.flowManager.entity.FileRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileStatusResponse {
    private Long fileId;
    private FileRecord.FileStatus status;
    private String originalPath;
    private String convertedPath;
    private String message;
}
