package com.techshare.service.ImageStorage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {

    String saveImage(MultipartFile multipartFile);
    void deleteImage(String image);
    public byte[] getImage(String image);
}
