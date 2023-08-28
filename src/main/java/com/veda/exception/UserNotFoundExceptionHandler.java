package com.veda.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
 

@Provider
public class UserNotFoundExceptionHandler implements ExceptionMapper<UserNotFoundException> {
    @Override
    public Response toResponse(UserNotFoundException e) {
        return  Response.status (Response.Status.FORBIDDEN).build();
    }
}
