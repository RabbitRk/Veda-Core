package com.veda.entity.master;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


// TODO: Security context should be extended for implementing the created At and updated At

@MappedSuperclass
@Data
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    public Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    public Date updatedAt;

    @Column(name = "created_by")
    public String createdBy;

    @Column(name = "updated_by")
    public String updatedBy;

    @Column(columnDefinition = "boolean default true")
    public Boolean active;

    @PrePersist
    protected void onCreate() {
        updatedAt = createdAt = new Date();
        createdBy = updatedBy = "";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
        updatedBy = "";
    }

}