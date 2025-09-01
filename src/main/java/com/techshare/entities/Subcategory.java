package com.techshare.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    private Long subcategory_id;

    @Column(unique = true)
    @NotBlank(message = "El nombre de la subcategoría es obligatorio")
    private String name;

    @NotBlank(message = "La imagen es obligatoria")
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
    private List<Product> products;

    public List<Product> getMaterials() {
        return products;
    }

    public void setMaterials(List<Product> products) {
        this.products = products;
    }

    public Long getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(Long subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
