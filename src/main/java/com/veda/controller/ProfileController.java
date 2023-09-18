package com.veda.controller;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;

import com.veda.config.EntityCopyUtils;
import com.veda.entity.master.Profile;
import com.veda.repository.master.ProfileRepository;

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

@Path("/api/profile")
@RolesAllowed("Admin")
@Tag(name = "Profiles", description = "Profile Endpoints")
public class ProfileController {

    @Inject
    ProfileRepository profileRepository;

    @Inject
    EntityCopyUtils entityCopyUtils;

    @GET
    public Response list(
            @DefaultValue("0") @QueryParam("pageNo") int pageNo,
            @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        Iterable<Profile> user = profileRepository.findAll(PageRequest.of(pageNo, pageSize));
        return Response.ok(user).status(200).build();
    }

    @GET
    @Path("/{id}")
    public Response getByID(@PathParam("id") UUID id) {
        Optional<Profile> optional = profileRepository.findById(id);

        if (optional.isPresent()) {
            Profile profile = optional.get();
            return Response.ok(profile).status(200).build();
        }

        throw new IllegalArgumentException("No profile with id " + id + " exists");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response add(Profile profile) {
        if (profile.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        profileRepository.save(profile);
        return Response.ok(profile).status(201).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") UUID id) {
        Optional<Profile> entity = profileRepository.findById(id);
        if (!entity.isPresent()) {
            throw new WebApplicationException("profile with id of " + id + " does not exist.", 404);
        }
        profileRepository.delete(entity.get());
        return Response.status(204).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, Profile greeting) {
        Optional<Profile> optional = profileRepository.findById(id);

        if (optional.isPresent()) {
            Profile profile = optional.get();
            entityCopyUtils.copyProperties(profile, greeting);
            Profile updateUsers = profileRepository.save(profile);
            return Response.ok(updateUsers).status(200).build();
        }

        throw new IllegalArgumentException("No profile with id " + id + " exists");
    }

}
