package com.veda.entity.master;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profile extends BaseEntity {
    
    private String profileName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
