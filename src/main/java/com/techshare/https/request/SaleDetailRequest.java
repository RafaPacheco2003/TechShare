package com.techshare.https.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SaleDetailRequest {
    @NotNull
    private Long material_id;

    @NotNull
    @Min(1)
    private Integer quantity;

    

    public Long getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(Long material_id) {
        this.material_id = material_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
} 