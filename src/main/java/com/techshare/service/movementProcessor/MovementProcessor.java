package com.techshare.service.movementProcessor;

import com.techshare.entities.Material;
import com.techshare.http.request.MovementRequest;

public abstract class MovementProcessor {
    public abstract void applyMovement(Material material, MovementRequest movementRequest);
}