package com.techshare.mappers.movement;

import com.techshare.dto.MovementDTO;
import com.techshare.entities.Movement;
import com.techshare.http.request.MovementRequest;

public interface ConvertMovement {
    Movement convertMovementRequestToMovementEntity(MovementRequest movementRequest);

    void convertUpdateMovementRequestToMovement(MovementRequest movementRequest, Movement existingMovement);

    MovementDTO convertMovementEntityToMovementDTO(Movement movement);
}
