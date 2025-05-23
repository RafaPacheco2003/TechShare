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

    private Integer defaultDurationDays;

    @OneToMany(mappedBy = "membership")
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "membership")
    private Set<Material> materials = new HashSet<>();

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
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


}