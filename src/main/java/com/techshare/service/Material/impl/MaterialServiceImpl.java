package com.techshare.service.Material.impl;

import com.techshare.DTO.MaterialDTO;
import com.techshare.convert.Material.ConvertMaterial;
import com.techshare.entities.Material;// Nueva excepción personalizada
import com.techshare.http.request.MaterialRequest;
import com.techshare.repositories.MaterialRepository;
import com.techshare.service.ImageStorage.ImageStorage;  // Servicio para manejar las imágenes
import com.techshare.service.Material.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ImageStorage imageStorage;  // Autowired del servicio de imágenes

    @Autowired
    private ConvertMaterial convertMaterial;

    @Override
    public MaterialDTO createMaterial(MaterialRequest materialRequest, MultipartFile multipartFile) {
        // Guardar la imagen y obtener el nombre
        saveImage(materialRequest, multipartFile);

        // Convertir la solicitud en entidad
        Material materialEntity = convertMaterial.convertMaterialRequestToMaterial(materialRequest);

        // Guardar el material en la base de datos
        Material savedMaterial = materialRepository.save(materialEntity);

        // Convertir la entidad guardada en DTO y devolverla
        return convertMaterial.convertMaterialToMaterialDTO(savedMaterial);
    }

    @Override
    public Optional<MaterialDTO> getMaterialById(Long id) {
        return materialRepository.findById(id)
                .map(material -> {
                    MaterialDTO materialDTO = convertMaterial.convertMaterialToMaterialDTO(material);
                    // Crear URL de imagen y establecerla en el DTO
                    String imageUrl = "http://localhost:8080/images/" + material.getImage();
                    materialDTO.setImage(imageUrl);
                    return Optional.of(materialDTO);
                })
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + id));  // Excepción personalizada
    }

    @Override
    public Optional<MaterialDTO> updateMaterial(Long id, MaterialRequest materialRequest) {
        return materialRepository.findById(id)
                .map(existingMaterial -> {
                    convertMaterial.convertUpdateMaterialRequestToMaterial(materialRequest, existingMaterial);
                    Material updatedMaterial = materialRepository.save(existingMaterial);  // Guardar el material actualizado
                    return Optional.of(convertMaterial.convertMaterialToMaterialDTO(updatedMaterial));  // Devolver el DTO actualizado
                })
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + id));
    }

    @Override
    public List<MaterialDTO> getAllMaterials() {
        return materialRepository.findAll().stream()
                .map(material -> {
                    MaterialDTO materialDTO = convertMaterial.convertMaterialToMaterialDTO(material);
                    // Crear URL de la imagen
                    String imageUrl = "http://localhost:8080/images/" + material.getImage();
                    materialDTO.setImage(imageUrl);
                    return materialDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new RuntimeException("Material not found with ID: " + id);
        }
        materialRepository.deleteById(id);
    }

    @Override
    public void saveImage(MaterialRequest materialRequest, MultipartFile multipartFile) {
        // Guardar la imagen usando el servicio de almacenamiento y obtener la ruta
        String imagePath = imageStorage.saveImage(multipartFile);

        // Extraer solo el nombre del archivo de la ruta completa
        String imageName = Paths.get(imagePath).getFileName().toString();

        // Asignar el nombre de la imagen al materialRequest
        materialRequest.setImage(imageName);
    }
}
