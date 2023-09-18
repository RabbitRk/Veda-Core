package com.veda.service.jwt;

import com.veda.model.auth.User;

public interface IJwtTokenUtil {
    public String generateToken(User user);
    public User verifyToken(String token);
}
