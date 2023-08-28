package com.veda.model.auth;

import java.util.UUID;

import lombok.Data;

/*
 * Refresh token request is used for
 * 
 * - Token refresh
 * - Forgot password
 * 
 */
@Data
public class RefreshTokenRequest {
    private UUID userID;
    private String refreshToken;
}
