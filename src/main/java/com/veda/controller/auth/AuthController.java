package com.veda.controller.auth;

import com.veda.entity.master.Profile;
import com.veda.entity.master.Users;
import com.veda.exception.UserNotFoundException;
import com.veda.model.auth.LoginRequest;
import com.veda.model.auth.LoginResponse;
import com.veda.model.auth.RefreshTokenRequest;
import com.veda.model.auth.SignupRequest;
import com.veda.service.auth.AuthService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/authenticate")
    public Response authenticate(LoginRequest login) throws UserNotFoundException {

        LoginResponse response = authService.authenticate(login);

        return Response.ok(response).build();
    }

    @POST
    @Path("/sign-up")
    @Transactional
    public Response signUp(@Valid SignupRequest signUp) {

        Users users = authService.createUser(signUp);

        Profile profile = authService.createProfile(users);

        return Response.ok(profile).build();
    }

    @POST
    @Path("/refresh-token")
    public Response refreshToken(RefreshTokenRequest refreshToken) throws UserNotFoundException {
        LoginResponse response = authService.refreshToken(refreshToken);

        return Response.ok(response).build();
    }

    @POST
    @Path("/forgot-password")
    public Response forgotPassword(RefreshTokenRequest refreshToken) {
        // TODO: Pending Implementation
        return Response.ok(refreshToken).build();
    }
}
