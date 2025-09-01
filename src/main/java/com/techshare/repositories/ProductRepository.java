package com.techshare.repositories;

import com.techshare.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT m FROM Product m WHERE m.subcategory.subcategory_id IN :subcategoryIds")
    List<Product> findBySubcategoryIds(@Param("subcategoryIds") List<Long> subcategoryIds);    @Query("SELECT m FROM Product m WHERE " +
           "(:categoryId IS NULL OR m.subcategory.category.category_id = :categoryId) AND " +
           "(:subcategoryId IS NULL OR m.subcategory.subcategory_id = :subcategoryId)")
    List<Product> findMaterialsWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("subcategoryId") Long subcategoryId,
            Sort sort);
            
    @Query("SELECT m FROM Product m")
    List<Product> findAllMaterials(Sort sort);

    Product findTopByOrderByPriceDesc();
    Product findTopByOrderByPriceAsc();
}
