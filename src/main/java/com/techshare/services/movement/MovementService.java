package com.techshare.services.movement;

import com.techshare.https.response.MovementDTO;
import com.techshare.https.request.MovementRequest;

import java.util.Optional;

public interface MovementService {
    MovementDTO createMovement(MovementRequest movementRequest);

    Optional<MovementDTO> getMovementById(Long id);

    Optional<MovementDTO> updateMovement(Long id, MovementRequest movementRequest);    org.springframework.data.domain.Page<MovementDTO> getAllMovements(int page, int size);

    void deleteMovement(Long id);
}
