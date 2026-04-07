package com.microservice.flowManager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResultEvent {
    private Long fileId;
    private ConversionStatus status;
    private String bucket;
    private String path;
    private String sourcePath;
}
