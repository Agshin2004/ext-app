package com.agshin.extapp.repositories;

import com.agshin.extapp.model.entities.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadaRepository extends JpaRepository<FileMetaData, Long> {
}
