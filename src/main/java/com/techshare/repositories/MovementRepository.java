package com.techshare.repositories;

import com.techshare.entities.Movement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    @Query("SELECT m FROM Movement m ORDER BY m.movement_id DESC")
    Page<Movement> findAllByOrderByMovementIdDesc(Pageable pageable);
}
