package com.techshare.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.techshare.entities.enums.SaleStatus;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sale_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private Set<SaleDetail> saleDetails = new HashSet<>();

    private Date saleDate;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private SaleStatus status;

    public Long getSale_id() {
        return sale_id;
    }

    public void setSale_id(Long sale_id) {
        this.sale_id = sale_id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<SaleDetail> getSaleDetails() {
        return saleDetails;
    }

    public void setSaleDetails(Set<SaleDetail> saleDetails) {
        this.saleDetails = saleDetails;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }
} 