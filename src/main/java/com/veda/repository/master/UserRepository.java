package com.veda.repository.master;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.veda.entity.master.Users;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface UserRepository extends PagingAndSortingRepository<Users, UUID> {

    Optional<Users> findByUserName(String username);

}