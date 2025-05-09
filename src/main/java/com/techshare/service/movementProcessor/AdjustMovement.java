package com.techshare.service.MovementProcessor;

import com.techshare.entities.Material;
import com.techshare.http.request.MovementRequest;
import org.springframework.stereotype.Component;

@Component("adjustMovementProcessor")
public class AdjustMovement extends MovementProcessor{
    @Override
    public void applyMovement(Material material, MovementRequest movementRequest) {
        int oldStock = material.getStock();
        material.setStock(movementRequest.getQuantity());
        int stockDifference = material.getStock() - oldStock;
        material.setBorrowable_stock(material.getBorrowable_stock() + stockDifference);
    }
}
