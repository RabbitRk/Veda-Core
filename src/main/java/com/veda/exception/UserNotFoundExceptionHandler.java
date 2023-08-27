package com.veda.exception;

import com.veda.model.BaseResponseBuilder;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
 

@Provider
public class UserNotFoundExceptionHandler implements ExceptionMapper<UserNotFoundException> {
    @Override
    public Response toResponse(UserNotFoundException e) {
        return  Response.status (Response.Status.FORBIDDEN).entity(new
                BaseResponseBuilder<String>().ofError("User not found").createBaseResponse()).build();
    }
}
