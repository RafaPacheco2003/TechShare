package com.techshare.services.imageValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageValidator {

    // Lista de extensiones permitidas
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    // Tamaño máximo permitido en bytes (5MB en este caso)
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    // Ancho y alto máximo (opcional)
    private static final int MAX_WIDTH = 1920;
    private static final int MAX_HEIGHT = 1080;

    public void validateImage(MultipartFile multipartFile) {
        // Validar tipo de archivo (MIME type)
        String contentType = multipartFile.getContentType();
        if (contentType == null || !contentType.startsWith("image")) {
            throw new IllegalArgumentException("El archivo no es una imagen válida.");
        }

        // Validar el tamaño de la imagen
        if (multipartFile.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("El tamaño de la imagen excede el límite permitido de 5MB.");
        }

        // Validar la extensión de archivo
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename != null && !isExtensionValid(originalFilename)) {
            throw new IllegalArgumentException("La imagen debe tener una extensión válida (jpg, jpeg, png, gif).");
        }

        // Validar dimensiones de la imagen
        validateImageDimensions(multipartFile);
    }

    // Verifica si la extensión del archivo es permitida
    private boolean isExtensionValid(String filename) {
        String extension = getExtension(filename);
        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }

    // Obtiene la extensión de archivo
    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    // Verifica las dimensiones de la imagen (ancho y alto)
    private void validateImageDimensions(MultipartFile multipartFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            if (width > MAX_WIDTH || height > MAX_HEIGHT) {
                throw new IllegalArgumentException("La imagen excede las dimensiones permitidas de " + MAX_WIDTH + "x" + MAX_HEIGHT + " píxeles.");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo leer la imagen para validar sus dimensiones.", e);
        }
    }
}
