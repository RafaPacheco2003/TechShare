package com.techshare.convert.Movement;

import com.techshare.DTO.MovementDTO;
import com.techshare.entities.Movement;
import com.techshare.http.request.MovementRequest;

public interface ConvertMovement {
    Movement convertMovementRequestToMovementEntity(MovementRequest movementRequest);

    void convertUpdateMovementRequestToMovement(MovementRequest movementRequest, Movement existingMovement);

    MovementDTO convertMovementEntityToMovementDTO(Movement movement);
}
