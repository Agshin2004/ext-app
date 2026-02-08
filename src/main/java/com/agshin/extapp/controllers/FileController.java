package com.agshin.extapp.controllers;

import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.entities.FileMetaData;
import com.agshin.extapp.model.response.GenericResponse;
import com.agshin.extapp.services.LocalFileStorageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/files")
public class FileController {
    private final LocalFileStorageService storageService;

    public FileController(LocalFileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<GenericResponse<FileMetaData>> uploadFile(@RequestParam("file") MultipartFile file) {
        var response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                storageService.storeFile(file),
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(response);
    }


    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) throws IOException {
        FileMetaData meta = storageService.getMetadata(id);
        Path path = storageService.loadFileAsPath(id);

        InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(meta.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=".concat(meta.getFilename()))
                .body(resource);
    }

    @GetMapping("/{id}/inline")
    public ResponseEntity<InputStreamResource> inlineFile(@PathVariable Long id) throws IOException {
        FileMetaData meta = storageService.getMetadata(id);
        Path path = storageService.loadFileAsPath(id);

        InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(meta.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=".concat(meta.getFilename()))
                .body(resource);
    }
}
