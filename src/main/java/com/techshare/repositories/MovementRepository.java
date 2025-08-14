package com.techshare.repositories;

import com.techshare.entities.Movement;
import com.techshare.entities.enums.MoveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    // Método único que maneja ambos casos (con o sin filtro)
    @Query("SELECT m FROM Movement m " +
            "WHERE (:moveType IS NULL OR m.moveType = :moveType) " +
            "ORDER BY m.movement_id DESC")
    Page<Movement> findAllMovements(
            @Param("moveType") MoveType moveType,
            Pageable pageable
    );
    


}