package com.veda.controller;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;

import com.veda.config.EntityCopyUtils;
import com.veda.entity.master.Users;
import com.veda.repository.master.UserRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * The UserController class is a Java class that represents the API endpoints
 * for managing global users.
 */
@Path("/api/users")
@RolesAllowed("Admin")
@Tag(name = "Users", description = "Global Users Endpoints")
public class UserController {

    @Inject
    UserRepository userRepository;

    @Inject
    EntityCopyUtils entityCopyUtils;

    @GET
    public Response list(
            @DefaultValue("0") @QueryParam("pageNo") int pageNo,
            @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        Iterable<Users> user = userRepository.findAll(PageRequest.of(pageNo, pageSize));
        return Response.ok(user).status(200).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response add(Users users) {
        if (users.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        userRepository.save(users);
        return Response.ok(users).status(201).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") UUID id) {
        Optional<Users> entity = userRepository.findById(id);
        if (!entity.isPresent()) {
            throw new WebApplicationException("users with id of " + id + " does not exist.", 404);
        }
        userRepository.delete(entity.get());
        return Response.status(204).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, Users greeting) {
        Optional<Users> optional = userRepository.findById(id);

        if (optional.isPresent()) {
            Users users = optional.get();
            entityCopyUtils.copyProperties(users, greeting);
            Users updateUsers = userRepository.save(users);
            return Response.ok(updateUsers).status(200).build();
        }

        throw new IllegalArgumentException("No users with id " + id + " exists");
    }

}
