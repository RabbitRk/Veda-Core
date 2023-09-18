package com.veda.entity.master;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AgeGroup extends BaseEntity {
    public String ageGroupName;
    public String ageGroupDescription;
}
