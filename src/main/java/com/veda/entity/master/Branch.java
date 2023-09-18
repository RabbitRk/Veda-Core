package com.veda.entity.master;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Branch extends BaseEntity {
    
    public String branchName;

    public String branchDescription;

    @OneToOne
    public AgeGroup ageGroup;
}
