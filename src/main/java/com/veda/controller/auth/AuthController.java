package com.veda.controller.auth;

import org.jboss.logmanager.Logger;

import com.veda.exception.UserNotFoundException;
import com.veda.model.BaseResponse;
import com.veda.model.BaseResponseBuilder;
import com.veda.model.LoginRequest;
import com.veda.model.LoginResponse;
import com.veda.model.Status;
import com.veda.model.User;
import com.veda.model.UserBuilder;
import com.veda.service.Jwt.IJwtTokenUtil;
import com.veda.service.user.IUserService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
public class AuthController {

    private static final Logger LOG = Logger.getLogger("AuthController");

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

        LOG.info(login.getUserId());

        final User user = userService.findUser(login.getUserId(), login.getPassword());
        final String token = jwtTokenUtil.generateToken(user);
        BaseResponse<LoginResponse> baseResponse = new BaseResponseBuilder<LoginResponse>()
                .setData(new LoginResponse(token)).setStatus(Status.SUCCESS).createBaseResponse();
        return Response.ok(baseResponse).build();
    }
}
