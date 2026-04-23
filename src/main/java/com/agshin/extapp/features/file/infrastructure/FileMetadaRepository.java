package com.agshin.extapp.features.file.infrastructure;

import com.agshin.extapp.features.file.domain.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadaRepository extends JpaRepository<FileMetaData, Long> {
}
