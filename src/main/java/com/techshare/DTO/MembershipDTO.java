package com.techshare.DTO;

import java.util.HashSet;
import java.util.Set;

public class MembershipDTO {

    private Long membership_id;
    private String name;
    private String description;
    private Integer defaultDurationDays;
    private Integer percentage;

    // Puedes agregar esto si quieres retornar los IDs o nombres de los usuarios asociados
    private Set<String> userNames; // o Set<Long> userIds;
    private Set<Long> materialIds = new HashSet<>(); // Added for material relationship

    // Getters y setters

    public Set<Long> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(Set<Long> materialIds) {
        this.materialIds = materialIds;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
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

    public Set<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(Set<String> userNames) {
        this.userNames = userNames;
    }
}
