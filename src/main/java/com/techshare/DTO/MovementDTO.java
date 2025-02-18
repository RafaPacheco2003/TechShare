package com.techshare.DTO;

import com.techshare.entities.Material;
import com.techshare.entities.MoveType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    /*
        @ManyToOne
        @JoinColumn(name = "usuario_id")
        private Usuario usuario;
    */
    private Long material_id;

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

    public Long getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(Long material_id) {
        this.material_id = material_id;
    }
}
