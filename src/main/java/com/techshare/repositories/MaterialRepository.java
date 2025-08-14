package com.techshare.repositories;

import com.techshare.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query("SELECT m FROM Material m WHERE m.subcategory.subcategory_id IN :subcategoryIds")
    List<Material> findBySubcategoryIds(@Param("subcategoryIds") List<Long> subcategoryIds);    @Query("SELECT m FROM Material m WHERE " +
           "(:categoryId IS NULL OR m.subcategory.category.category_id = :categoryId) AND " +
           "(:subcategoryId IS NULL OR m.subcategory.subcategory_id = :subcategoryId)")
    List<Material> findMaterialsWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("subcategoryId") Long subcategoryId,
            Sort sort);
            
    @Query("SELECT m FROM Material m")
    List<Material> findAllMaterials(Sort sort);

    Material findTopByOrderByPriceDesc();
    Material findTopByOrderByPriceAsc();
}
