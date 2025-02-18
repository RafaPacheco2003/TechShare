package com.techshare.http.request;

public class MaterialRequest{

    private String name;
    private String descripcion;
    private Integer stock;
    private Long subcategory;
    private Double priece;
    private String image;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Long subcategory) {
        this.subcategory = subcategory;
    }

    public Double getPriece() {
        return priece;
    }

    public void setPriece(Double priece) {
        this.priece = priece;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}