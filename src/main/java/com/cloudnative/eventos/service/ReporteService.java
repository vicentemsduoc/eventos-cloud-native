package com.cloudnative.eventos.service;

import com.cloudnative.eventos.model.Compra;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final S3Service s3Service;
    private static final String EFS_PATH = "/app/efs";

    public String generarYSubirResumen(Compra compra) throws IOException {
        String contenido = generarContenidoResumen(compra);
        String nombreArchivo = "resumen-compra-" + compra.getId() + ".txt";

        Path rutaEfs = Paths.get(EFS_PATH, nombreArchivo);
        Files.writeString(rutaEfs, contenido, StandardCharsets.UTF_8);

        MultipartFile archivo = new InMemoryMultipartFile(
                nombreArchivo,
                nombreArchivo,
                "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8));

        String carpetaS3 = "evento-" + compra.getEvento().getId() + "/compra-" + compra.getId();
        return s3Service.uploadFile(carpetaS3, archivo);
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
        public java.io.InputStream getInputStream() {
            return new java.io.ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException {
            Files.write(dest.toPath(), content);
        }
    }
}