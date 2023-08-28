package com.veda.entity.master;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Roles extends BaseEntity {
    private String roleName;
    private String roleType;
}
