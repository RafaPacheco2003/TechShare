package com.techshare.services.movement;

import com.techshare.entities.enums.MoveType;
import com.techshare.https.response.MovementDTO;
import com.techshare.https.request.MovementRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MovementService {
    MovementDTO createMovement(MovementRequest movementRequest);

    Optional<MovementDTO> getMovementById(Long id);

    Optional<MovementDTO> updateMovement(Long id, MovementRequest movementRequest);

    Page<MovementDTO> getAllMovements(int page, int size, MoveType moveType);

    void deleteMovement(Long id);
}
