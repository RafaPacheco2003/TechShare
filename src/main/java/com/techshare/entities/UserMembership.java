package com.techshare.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class UserMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_membership_id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @ManyToOne
    @JoinColumn(name = "membership_id")
    private Membership membership;
    
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    
    private boolean isActive;
    
    // Comentarios adicionales o información de aprobación
    private String notes;

    // Getters y setters
    public Long getUser_membership_id() {
        return user_membership_id;
    }

    public void setUser_membership_id(Long user_membership_id) {
        this.user_membership_id = user_membership_id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}