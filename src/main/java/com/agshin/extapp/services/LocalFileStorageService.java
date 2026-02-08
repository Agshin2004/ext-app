package com.agshin.extapp.services;

import com.agshin.extapp.config.FileStorageProperties;
import com.agshin.extapp.exceptions.DataNotFoundException;
import com.agshin.extapp.exceptions.ValidationException;
import com.agshin.extapp.model.entities.FileMetaData;
import com.agshin.extapp.repositories.FileMetadaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService {
    private final Path fileStorageLocation;
    private final FileMetadaRepository fileMetadaRepository;

    public LocalFileStorageService(FileStorageProperties properties, FileMetadaRepository repo) {
        this.fileStorageLocation = Paths.get(properties.getUploadDir());
        this.fileMetadaRepository = repo;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload dir");
        }
    }

    public FileMetaData storeFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename())
                .concat("-")
                .concat(UUID.randomUUID().toString());

        if (filename.contains("..")) {
            throw new IllegalArgumentException("invalid filename");
        }

        String contentType = file.getContentType();
        if (!contentType.startsWith("image/") && !contentType.equals("application/pdf")) {
            throw new ValidationException("invalid file type");
        }

        Path targetLocation = fileStorageLocation.resolve(filename);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileMetaData meta = new FileMetaData();
            meta.setFilename(filename);
            meta.setFileType(contentType);
            meta.setStoragePath(targetLocation.toString());
            meta.setSize(file.getSize());

            return fileMetadaRepository.save(meta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path loadFileAsPath(Long id) {
        FileMetaData meta = fileMetadaRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("file not found"));
        return Path.of(meta.getStoragePath());
    }

    public FileMetaData getMetadata(Long id) {
        return fileMetadaRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("file not found"));
    }
}
