package com.microservice.flowManager.dto;

import com.microservice.flowManager.entity.FileRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UploadResponse {
    private Long fileId;
    private FileRecord.FileStatus status;
    private String message;
}
