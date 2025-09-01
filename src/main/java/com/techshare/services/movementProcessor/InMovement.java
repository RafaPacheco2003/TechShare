package com.techshare.services.movementProcessor;

import com.techshare.entities.Product;
import com.techshare.https.request.MovementRequest;
import org.springframework.stereotype.Component;

@Component("inMovementProcessor")
public class InMovement extends com.techshare.services.movementProcessor.MovementProcessor {

    @Override    public void applyMovement(Product product, MovementRequest movementRequest) {
        product.setStock(product.getStock() + movementRequest.getQuantity());
    }
}
