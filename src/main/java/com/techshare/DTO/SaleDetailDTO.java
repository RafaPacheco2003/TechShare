package com.techshare.DTO;

public class SaleDetailDTO {
    private Long saleDetail_id;
    private Long material_id;
    private String materialName;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;

    public Long getSaleDetail_id() {
        return saleDetail_id;
    }

    public void setSaleDetail_id(Long saleDetail_id) {
        this.saleDetail_id = saleDetail_id;
    }

    public Long getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(Long material_id) {
        this.material_id = material_id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
} 