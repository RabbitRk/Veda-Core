package com.veda.service.Jwt;

import com.veda.model.auth.User;

public interface IJwtTokenUtil {
    public String generateToken(User user);
    public User verifyToken(String token);
}
