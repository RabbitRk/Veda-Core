package com.veda.entity.course;

import com.veda.entity.master.BaseEntity;
import com.veda.entity.master.Branch;
import com.veda.entity.master.Category;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Course extends BaseEntity {

    // TODO: Have to impletement sequence in the database

    private String courseName;

    private String courseDescription;

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private Category category;
}
