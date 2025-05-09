package com.techshare.service.MovementProcessor;

import com.techshare.entities.Material;
import com.techshare.http.request.MovementRequest;
import org.springframework.stereotype.Component;

@Component("outMovementProcessor")
public class OutMovement extends MovementProcessor{
    @Override
    public void applyMovement(Material material, MovementRequest movementRequest) {
        material.setStock(material.getStock() - movementRequest.getQuantity());
        material.setBorrowable_stock(material.getBorrowable_stock() - movementRequest.getQuantity());

    }
}
