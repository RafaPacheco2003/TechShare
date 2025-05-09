package com.techshare.service.ImageStorage.impl;

import com.techshare.service.ImageStorage.ImageStorage;
import com.techshare.service.ImageValidator.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageImpl implements ImageStorage {

    @Value("${storage.location}")
    private String storageLocation;



    @Autowired
    private ImageValidator imageValidator;


    @Override
    public String saveImage(MultipartFile multipartFile) {
        //imageValidator.validateImage(multipartFile);
        Path path = Paths.get(storageLocation, multipartFile.getOriginalFilename());
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }

        // Retornar la ruta absoluta de la imagen guardada
        return path.toAbsolutePath().toString();
    }


    @Override
    public void deleteImage(String image) {
        Path path = Paths.get(storageLocation, image);
        File file = path.toFile();
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new RuntimeException("Failed to delete image: " + image);
            }
        }
    }

    @Override
    public byte[] getImage(String image) {
        Path path = Paths.get(storageLocation, image);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image: " + image, e);
        }
    }
}
