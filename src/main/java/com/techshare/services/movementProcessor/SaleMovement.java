package com.techshare.services.MovementProcessor;

import com.techshare.entities.Material;
import com.techshare.http.request.MovementRequest;
import org.springframework.stereotype.Component;

@Component("saleMovementProcessor")
public class SaleMovement extends MovementProcessor {
    @Override
    public void applyMovement(Material material, MovementRequest movementRequest) {
        material.setStock(material.getStock() - movementRequest.getQuantity());
    }
}
