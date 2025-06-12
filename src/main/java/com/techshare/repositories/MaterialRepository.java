package com.techshare.repositories;

import com.techshare.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query("SELECT m FROM Material m WHERE m.subcategory.subcategory_id IN :subcategoryIds")
    List<Material> findBySubcategoryIds(@Param("subcategoryIds") List<Long> subcategoryIds);
}
