package com.veda.repository.master;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.veda.entity.master.RefreshToken;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID>{

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    
}