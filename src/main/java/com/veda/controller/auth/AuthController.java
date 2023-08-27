package com.veda.controller.auth;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;

import com.veda.entity.RefreshToken;
import com.veda.exception.UserNotFoundException;
import com.veda.model.LoginRequest;
import com.veda.model.LoginResponse;
import com.veda.model.User;
import com.veda.model.UserBuilder;
import com.veda.service.Jwt.IJwtTokenUtil;
import com.veda.service.user.IUserService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
public class AuthController { 

    @ConfigProperty(name = "jwt.refreshToken.expiration-time")
    int refreshTokenExpiryTime;

    @Inject
    IJwtTokenUtil jwtTokenUtil;

    @Inject
    IUserService userService;

    @GET
    @Path("/token")
    public String tokenGenerate() {
        User user = new UserBuilder().setId("1").setName("test").setRoles("Admin").createUser();
        return jwtTokenUtil.generateToken(user);
    }

    @GET
    @Path("/verify")
    public User verification(@QueryParam("token") String token) {
        return jwtTokenUtil.verifyToken(token);
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest login) throws UserNotFoundException {

        LocalDateTime now = LocalDateTime.now();
 
        final User user = userService.findUser(login.getUserId(), login.getPassword());
        final String token = jwtTokenUtil.generateToken(user);


        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(generateRefreshToken());
        refreshToken.setExpiryDate(Timestamp.valueOf(now.plusDays(refreshTokenExpiryTime)));
        // TODO: Repo call for saving the refresh token with the expiry time - 7 days
        // <>


        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(refreshToken.getRefreshToken());

        return Response.ok(loginResponse).build();
    }


    String generateRefreshToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

     private Date convertLocalDateTimeToDate(LocalDateTime now) {
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }
}
