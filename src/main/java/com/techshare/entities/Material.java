package com.techshare.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long material_id;
    private String name;
    private String descripcion;
    private Integer stock;
    private Integer borrowable_stock;
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;
    private Double price;
    private String image;
    // private Usuario usario; //Aun no se implementa hasta el security


    // Relación muchos a muchos con Membership
    @ManyToMany(mappedBy = "restrictedMaterials")
    private Set<Membership> requiredMemberships = new HashSet<>();

    
    public Set<Membership> getRequiredMemberships() {
        return requiredMemberships;
    }

    public void setRequiredMemberships(Set<Membership> requiredMemberships) {
        this.requiredMemberships = requiredMemberships;
    }

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

    public Integer getBorrowable_stock() {
        return borrowable_stock;
    }

    public void setBorrowable_stock(Integer borrowable_stock) {
        this.borrowable_stock = borrowable_stock;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
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
}
