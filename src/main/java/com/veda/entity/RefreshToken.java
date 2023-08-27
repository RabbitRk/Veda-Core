package com.veda.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshToken  extends BaseEntity{
    public String refreshToken;
    public Timestamp expiryDate;
}