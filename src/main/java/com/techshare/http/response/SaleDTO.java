package com.techshare.dto;

import java.util.Date;
import java.util.Set;
import com.techshare.entities.enums.SaleStatus;

public class SaleDTO {
    private Long sale_id;
    private Long user_id;
    private String userName;
    private Date saleDate;
    private Double totalAmount;
    private SaleStatus status;
    private Set<SaleDetailDTO> saleDetails;

    public Long getSale_id() {
        return sale_id;
    }

    public void setSale_id(Long sale_id) {
        this.sale_id = sale_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Set<SaleDetailDTO> getSaleDetails() {
        return saleDetails;
    }

    public void setSaleDetails(Set<SaleDetailDTO> saleDetails) {
        this.saleDetails = saleDetails;
    }
} 