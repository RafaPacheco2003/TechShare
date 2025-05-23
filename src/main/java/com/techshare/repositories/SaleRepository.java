package com.techshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techshare.entities.Sale;
import com.techshare.entities.UserEntity;
import com.techshare.entities.enums.SaleStatus;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByUser(UserEntity user);
    List<Sale> findByStatus(SaleStatus status);
} 