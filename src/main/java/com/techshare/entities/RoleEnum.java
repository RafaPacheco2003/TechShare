package com.techshare.entities;

import jakarta.persistence.Entity;
import lombok.*;


@Getter
public enum RoleEnum {

    ADMIN,
    USER,
    INVITED,
    DEVELOPER
}
