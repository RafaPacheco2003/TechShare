package com.techshare.services.movementProcessor;

import com.techshare.entities.Product;
import com.techshare.https.request.MovementRequest;

public abstract class MovementProcessor {
    public abstract void applyMovement(Product product, MovementRequest movementRequest);
}