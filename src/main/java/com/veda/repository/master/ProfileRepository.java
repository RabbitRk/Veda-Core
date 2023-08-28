package com.veda.repository.master;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.veda.entity.master.Profile;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface ProfileRepository extends CrudRepository<Profile, UUID>{
    
}