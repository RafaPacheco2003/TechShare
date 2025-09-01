package com.techshare.services.movementProcessor;

import com.techshare.entities.Product;
import com.techshare.https.request.MovementRequest;
import org.springframework.stereotype.Component;

@Component("adjustMovementProcessor")
public class AdjustMovement extends com.techshare.services.movementProcessor.MovementProcessor {
    @Override    public void applyMovement(Product product, MovementRequest movementRequest) {
        product.setStock(movementRequest.getQuantity());
    }
}
