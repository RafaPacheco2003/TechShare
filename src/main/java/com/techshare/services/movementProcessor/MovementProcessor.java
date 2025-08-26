package com.techshare.services.movementProcessor;

import com.techshare.entities.Material;
import com.techshare.https.request.MovementRequest;

public abstract class MovementProcessor {
    public abstract void applyMovement(Material material, MovementRequest movementRequest);
}