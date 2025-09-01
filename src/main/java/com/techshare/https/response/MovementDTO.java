package com.techshare.https.response;

import com.techshare.entities.enums.MoveType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

public class MovementDTO {

    private Long movement_id;

    private MoveType moveType;

    private int quantity;

    private String comment;

    @Temporal(TemporalType.DATE)
    private Date date;

    private Long product_id;
    private String product_name;

    private Long user_id;
    private String user_name;


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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

    public Long getProduct_id() {
        return product_id;
    }    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
