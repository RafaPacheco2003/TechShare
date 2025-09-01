package com.techshare.entities;

import jakarta.persistence.*;

@Entity
public class SaleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleDetail_id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Product product;

    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;


    

    public Long getSaleDetail_id() {
        return saleDetail_id;
    }

    public void setSaleDetail_id(Long saleDetail_id) {
        this.saleDetail_id = saleDetail_id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getMaterial() {
        return product;
    }

    public void setMaterial(Product product) {
        this.product = product;
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