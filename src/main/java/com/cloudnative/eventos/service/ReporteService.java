package com.cloudnative.eventos.service;

import com.cloudnative.eventos.model.Compra;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final S3Service s3Service;

    public String generarYSubirResumen(Compra compra) throws IOException {
        String contenido = generarContenidoResumen(compra);
        String nombreArchivo = "resumen-compra-" + compra.getId() + ".txt";

        // Convertir el texto en un MultipartFile en memoria
        MultipartFile archivo = new InMemoryMultipartFile(
                nombreArchivo,
                nombreArchivo,
                "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8));

        // Carpeta = ID de la compra
        return s3Service.uploadFile(compra.getId().toString(), archivo);
    }

    private String generarContenidoResumen(Compra compra) {
        return """
                ===== RESUMEN DE COMPRA =====
                ID Compra: %d
                Evento: %s
                Fecha del evento: %s
                Cantidad de entradas: %d
                Precio unitario: %s
                Precio total: %s
                Fecha de compra: %s
                =============================
                """.formatted(
                compra.getId(),
                compra.getEvento().getNombre(),
                compra.getEvento().getFecha(),
                compra.getCantidad(),
                compra.getPrecioUnitario(),
                compra.getPrecioTotal(),
                compra.getFechaCompra());
    }

    // Clase interna para simular un MultipartFile desde bytes
    private static class InMemoryMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public InMemoryMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public ByteArrayInputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException {
            java.nio.file.Files.write(dest.toPath(), content);
        }
    }
}