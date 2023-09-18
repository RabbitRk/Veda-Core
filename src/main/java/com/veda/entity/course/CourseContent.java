package com.veda.entity.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veda.entity.master.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CourseContent extends BaseEntity{
    public String courseContentJson;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    public Course course;
}
