package com.techshare.service.Material;

import com.techshare.DTO.MaterialDTO;
import com.techshare.http.request.MaterialRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MaterialService {
    MaterialDTO createMaterial(MaterialRequest materialRequest, MultipartFile multipartFile);  // Añadir MultipartFile para la imagen
    Optional<MaterialDTO> getMaterialById(Long id);
    Optional<MaterialDTO> updateMaterial(Long id, MaterialRequest materialRequest);
    List<MaterialDTO> getAllMaterials();
    void deleteMaterial(Long id);
    void saveImage(MaterialRequest materialRequest, MultipartFile multipartFile);  // Método para guardar imagen
}
