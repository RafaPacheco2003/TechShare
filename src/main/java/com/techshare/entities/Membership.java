package com.techshare.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membership_id;
    
    private String name;
    
    private String description;
   
    
    // Fecha de expiración por defecto en días (opcional)
    private Integer defaultDurationDays;
    
    // Materiales que requieren esta membresía para ser prestados
    @ManyToMany
    @JoinTable(
        name = "membership_materials",
        joinColumns = @JoinColumn(name = "membership_id"),
        inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private Set<Material> restrictedMaterials = new HashSet<>();

    // Getters y setters
    public Long getMembership_id() {
        return membership_id;
    }

    public void setMembership_id(Long membership_id) {
        this.membership_id = membership_id;
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

    

    public Integer getDefaultDurationDays() {
        return defaultDurationDays;
    }

    public void setDefaultDurationDays(Integer defaultDurationDays) {
        this.defaultDurationDays = defaultDurationDays;
    }

    public Set<Material> getRestrictedMaterials() {
        return restrictedMaterials;
    }

    public void setRestrictedMaterials(Set<Material> restrictedMaterials) {
        this.restrictedMaterials = restrictedMaterials;
    }
}