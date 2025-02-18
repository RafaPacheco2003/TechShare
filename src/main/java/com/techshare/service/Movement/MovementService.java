package com.techshare.service.Movement;

import com.techshare.DTO.MovementDTO;
import com.techshare.http.request.MovementRequest;

import java.util.List;
import java.util.Optional;

public interface MovementService {
    MovementDTO createMovement(MovementRequest movementRequest);

    Optional<MovementDTO> getMovementById(Long id);

    Optional<MovementDTO> updateMovement(Long id, MovementRequest movementRequest);

    List<MovementDTO> getAllMovements();

    void deleteMovement(Long id);
}
