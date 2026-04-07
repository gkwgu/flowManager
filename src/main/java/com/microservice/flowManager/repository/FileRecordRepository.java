package com.microservice.flowManager.repository;

import com.microservice.flowManager.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRecordRepository extends JpaRepository<FileRecord,Long> {
}
