package com.veda.entity;

import com.veda.entity.master.BaseEntity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@Cacheable
// @NamedQueries({
//     @NamedQuery(name = "Greeting.like", query = "SELECT * FROM greeting where name like '%:greetingName%'")
// })
public class Greeting extends BaseEntity{
    public String name;
    public String nickname;


    // public static Greeting findByName(String name){
    //     return find("#Person.getByName", name).firstResult();
    // }
}
