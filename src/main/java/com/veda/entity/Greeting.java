package com.veda.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;

@Entity
@Cacheable
public class Greeting extends BaseEntity{
    public String name;
    public String nickname;
}
