package com.techshare.services.MovementProcessor;

import com.techshare.entities.Material;
import com.techshare.https.request.MovementRequest;
import org.springframework.stereotype.Component;

@Component("outMovementProcessor")
public class OutMovement extends MovementProcessor{
    @Override    public void applyMovement(Material material, MovementRequest movementRequest) {
        material.setStock(material.getStock() - movementRequest.getQuantity());
    }
}
