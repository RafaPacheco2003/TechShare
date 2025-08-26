package com.techshare.services.movementProcessor;

import com.techshare.entities.Material;
import com.techshare.https.request.MovementRequest;
import org.springframework.stereotype.Component;

@Component("adjustMovementProcessor")
public class AdjustMovement extends com.techshare.services.movementProcessor.MovementProcessor {
    @Override    public void applyMovement(Material material, MovementRequest movementRequest) {
        material.setStock(movementRequest.getQuantity());
    }
}
