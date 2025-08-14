package com.techshare.repositories;

import com.techshare.entities.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    @Query("SELECT s FROM Subcategory s WHERE s.category.category_id = :categoryId")
    List<Subcategory> findByCategoryId(@Param("categoryId") Long categoryId);
}
