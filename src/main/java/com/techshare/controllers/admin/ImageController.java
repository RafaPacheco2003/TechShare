package com.techshare.controllers.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Value("${storage.location}")
    private String storageLocation;

    @GetMapping("/{imageName}")
    public ResponseEntity<?> getImage(@PathVariable String imageName) {
        try {
            // Ruta completa donde se guarda la imagen
            Path path = Paths.get(storageLocation, imageName);

            // Verificar si la imagen existe
            if (!Files.exists(path)) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Imagen no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Leer el contenido de la imagen
            byte[] imageBytes = Files.readAllBytes(path);

            // Determinar el tipo MIME de la imagen
            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // Tipo genérico si no se puede determinar
            }

            // Devolver la imagen con el tipo MIME correspondiente
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(imageBytes);

        } catch (IOException e) {
            // Manejo de error en caso de fallar al leer la imagen
            Map<String, String> response = new HashMap<>();
            response.put("error", "No se pudo leer la imagen");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
