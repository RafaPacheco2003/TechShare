package com.techshare.convert.Material;


import com.techshare.DTO.MaterialDTO;
import com.techshare.entities.Material;
import com.techshare.http.request.MaterialRequest;
import com.techshare.entities.Subcategory;
import org.springframework.stereotype.Service;

@Service
public class ConvertMaterialImpl implements ConvertMaterial {

    @Override
    public Material convertMaterialRequestToMaterial(MaterialRequest materialRequest) {
        Material material = new Material();
        material.setName(materialRequest.getName());
        material.setDescripcion(materialRequest.getDescripcion());
        if(materialRequest.getStock()== null || materialRequest.getStock()== 0){
            material.setStock(0);
            material.setBorrowable_stock(0);
        }
        material.setStock(materialRequest.getStock());
        material.setBorrowable_stock(materialRequest.getStock());

        // Asumiendo que obtendrás el subcategory correspondiente por su ID
        Subcategory subcategory = new Subcategory();
        subcategory.setSubcategory_id(materialRequest.getSubcategory());
        material.setSubcategory(subcategory);

        material.setPrice(materialRequest.getPriece());
        material.setImage(materialRequest.getImage());
        return material;
    }

    @Override
    public void convertUpdateMaterialRequestToMaterial(MaterialRequest materialRequest, Material existingMaterial) {
        existingMaterial.setName(materialRequest.getName());
        existingMaterial.setDescripcion(materialRequest.getDescripcion());
        existingMaterial.setStock(materialRequest.getStock());

        // Asumiendo que también actualizarás el subcategory basado en el ID
        Subcategory subcategory = new Subcategory();
        subcategory.setSubcategory_id(materialRequest.getSubcategory());
        existingMaterial.setSubcategory(subcategory);

        existingMaterial.setPrice(materialRequest.getPriece());
        existingMaterial.setImage(materialRequest.getImage());
    }

    @Override
    public MaterialDTO convertMaterialToMaterialDTO(Material material) {
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterial_id(material.getMaterial_id());
        materialDTO.setName(material.getName());
        materialDTO.setDescription(material.getDescripcion());
        materialDTO.setStock(material.getStock());
        materialDTO.setBorrowable_stock(material.getBorrowable_stock());

        // Asumiendo que estás obteniendo el ID de subcategory desde la entidad
        materialDTO.setSubcategory(material.getSubcategory().getSubcategory_id());

        materialDTO.setPrice(material.getPrice());
        materialDTO.setImage(material.getImage());
        return materialDTO;
    }
}