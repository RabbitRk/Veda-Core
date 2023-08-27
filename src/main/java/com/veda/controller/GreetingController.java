package com.veda.controller;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.veda.config.EntityCopyUtils;
import com.veda.entity.Greeting;
import com.veda.repository.GreetingRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/api/greeting")
@Tag(name = "Greeting", description = "Greeting Operations")
// @SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "jwt")
public class GreetingController {

    @Inject
    GreetingRepository springGreetingRepository;

    @Inject
    EntityCopyUtils entityCopyUtils;

    @GET
    @RolesAllowed("Admin")
    public Response list() {
        Iterable<Greeting> greetings = springGreetingRepository.findAll();
        return Response.ok(greetings).status(200).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response add(Greeting fruit) {
        if (fruit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        springGreetingRepository.save(fruit);
        return Response.ok(fruit).status(201).build();
    }

    // c3886131-5a54-486a-84e2-8428c0c717bc

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") UUID id) {
        Optional<Greeting> entity = springGreetingRepository.findById(id);
        if (!entity.isPresent()) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        springGreetingRepository.delete(entity.get());
        return Response.status(204).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, Greeting greeting) {
        Optional<Greeting> optional = springGreetingRepository.findById(id);

        if (optional.isPresent()) {
            Greeting fruit = optional.get();
            entityCopyUtils.copyProperties(fruit, greeting);
           Greeting updateGreeting = springGreetingRepository.save(fruit);
           return Response.ok(updateGreeting).status(200).build();
        }

        throw new IllegalArgumentException("No Fruit with id " + id + " exists");
    }

}
