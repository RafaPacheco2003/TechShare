package com.techshare.repositories;

import com.techshare.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Query nativa segura
    @Query(value = "SELECT * FROM category ORDER BY category_id DESC LIMIT 7", nativeQuery = true)
    List<Category> findTop7Categories();
}