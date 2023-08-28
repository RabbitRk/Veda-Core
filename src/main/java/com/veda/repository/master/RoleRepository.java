package com.veda.repository.master;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.veda.entity.master.Roles;

public interface RoleRepository extends CrudRepository<Roles, UUID>{

    Optional<Roles> findByRoleName(String type);
    
}