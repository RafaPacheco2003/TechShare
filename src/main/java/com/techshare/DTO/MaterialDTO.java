package com.techshare.DTO;

public class MaterialDTO {
    private Long material_id;
    private String name;
    private String description;
    private Integer stock;
    private Integer borrowable_stock;
    private Long subcategory;
    private Double price;
    private String image;
    private Long membership_id;

    public Long getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(Long material_id) {
        this.material_id = material_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getBorrowable_stock() {
        return borrowable_stock;
    }

    public void setBorrowable_stock(Integer borrowable_stock) {
        this.borrowable_stock = borrowable_stock;
    }

    public Long getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Long subcategory) {
        this.subcategory = subcategory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getMembership_id() {
        return membership_id;
    }

    public void setMembership_id(Long membership_id) {
        this.membership_id = membership_id;
    }
}
