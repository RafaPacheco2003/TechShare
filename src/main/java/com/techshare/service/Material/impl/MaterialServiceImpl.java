package com.techshare.service.Material.impl;

import com.techshare.DTO.MaterialDTO;
import com.techshare.convert.Material.ConvertMaterial;
import com.techshare.entities.Material;
import com.techshare.http.request.MaterialRequest;
import com.techshare.repositories.MaterialRepository;
import com.techshare.service.Material.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ConvertMaterial convertMaterial;

    @Override
    public MaterialDTO createMaterial(MaterialRequest materialRequest) {
        Material materialEntity = convertMaterial.convertMaterialRequestToMaterial(materialRequest);
        Material savedMaterial = materialRepository.save(materialEntity);
        return convertMaterial.convertMaterialToMaterialDTO(savedMaterial);
    }

    @Override
    public Optional<MaterialDTO> getMaterialById(Long id) {
        return materialRepository.findById(id)
                .map(material -> Optional.of(convertMaterial.convertMaterialToMaterialDTO(material)))
                .orElseThrow(() -> new RuntimeException("Material not found"));
    }

    @Override
    public Optional<MaterialDTO> updateMaterial(Long id, MaterialRequest materialRequest) {
        return materialRepository.findById(id)
                .map(existingMaterial -> {
                    convertMaterial.convertUpdateMaterialRequestToMaterial(materialRequest, existingMaterial);
                    Material updatedMaterial = materialRepository.save(existingMaterial);
                    return Optional.of(convertMaterial.convertMaterialToMaterialDTO(updatedMaterial));
                })
                .orElseThrow(() -> new RuntimeException("Material not found"));
    }

    @Override
    public List<MaterialDTO> getAllMaterials() {
        return materialRepository.findAll().stream()
                .map(convertMaterial::convertMaterialToMaterialDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new RuntimeException("Material not found");
        }
        materialRepository.deleteById(id);
    }
}
