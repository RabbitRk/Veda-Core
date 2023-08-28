package com.veda.entity.master;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshToken  extends BaseEntity {
    public String userID;
    // @Column(name = "boolean default false")
    public String refreshToken;
    // @Column(name = "boolean default false")
    public Timestamp expiryDate;
}