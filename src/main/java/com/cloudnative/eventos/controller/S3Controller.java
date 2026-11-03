package com.cloudnative.eventos.controller;

import com.cloudnative.eventos.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    // Subir archivo a una carpeta (ej: número de resumen)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("folder") String folder,
            @RequestParam("file") MultipartFile file) throws IOException {

        String key = s3Service.uploadFile(folder, file);
        return ResponseEntity.ok("Archivo subido correctamente: " + key);
    }

    // Descargar archivo
    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("key") String key) {
        byte[] data = s3Service.downloadFile(key);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(resource);
    }

    // Eliminar archivo
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("key") String key) {
        s3Service.deleteFile(key);
        return ResponseEntity.ok("Archivo eliminado: " + key);
    }

    // Listar archivos (opcionalmente por prefijo/carpeta)
    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles(@RequestParam(value = "prefix", required = false) String prefix) {
        List<String> files = s3Service.listFiles(prefix == null ? "" : prefix);
        return ResponseEntity.ok(files);
    }

    // Mover o renombrar archivo
    @PostMapping("/move")
    public ResponseEntity<String> moveFile(
            @RequestParam("sourceKey") String sourceKey,
            @RequestParam("destinationKey") String destinationKey) {

        String newKey = s3Service.moveOrRenameFile(sourceKey, destinationKey);
        return ResponseEntity.ok("Archivo movido/renombrado a: " + newKey);
    }
}