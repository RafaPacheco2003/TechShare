package com.techshare.entities;

import com.techshare.entities.enums.MoveType;
import jakarta.persistence.*;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movement_id;

    private MoveType moveType;

    private int quantity;

    private String comment;

    @Column(name = "date", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity usuario) {
        this.user = usuario;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getMovement_id() {
        return movement_id;
    }

    public void setMovement_id(Long movement_id) {
        this.movement_id = movement_id;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Product getMaterial() {
        return product;
    }

    public void setMaterial(Product product) {
        this.product = product;
    }
}
