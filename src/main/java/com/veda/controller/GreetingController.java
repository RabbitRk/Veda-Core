package com.veda.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.veda.config.EntityCopyUtils;
import com.veda.entity.Greeting;
import com.veda.repository.GreetingRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
@RolesAllowed("Admin")
public class GreetingController {

    @Inject
    EntityManager entityManager;

    @Inject
    GreetingRepository greetingRepository;

    @Inject
    EntityCopyUtils entityCopyUtils;

    @GET
    public Response list() {
        Iterable<Greeting> greetings = greetingRepository.findAll();
        return Response.ok(greetings).status(200).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response add(Greeting fruit) {
        if (fruit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        greetingRepository.save(fruit);
        return Response.ok(fruit).status(201).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") UUID id) {
        Optional<Greeting> entity = greetingRepository.findById(id);
        if (!entity.isPresent()) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        greetingRepository.delete(entity.get());
        return Response.status(204).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, Greeting greeting) {
        Optional<Greeting> optional = greetingRepository.findById(id);

        if (optional.isPresent()) {
            Greeting fruit = optional.get();
            entityCopyUtils.copyProperties(fruit, greeting);
            Greeting updateGreeting = greetingRepository.save(fruit);
            return Response.ok(updateGreeting).status(200).build();
        }

        throw new IllegalArgumentException("No Fruit with id " + id + " exists");
    }

    @GET
    @Path("/{name}")
    @RolesAllowed("admin")
    public Response update(@PathParam("name") String name) {

        // The code snippet is performing a native SQL query to retrieve a list of Greeting entities
        // from the database based on a given name.
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM greeting where name like CONCAT('%', :greetingName, '%')",
                Greeting.class);
        query.setParameter("greetingName", name);

        List<Greeting> optional = query.getResultList();

        if (!optional.isEmpty()) {
            return Response.ok(optional).status(200).build();
        }

        throw new IllegalArgumentException("No Greeting is like " + name + " exists");
    }

//    @POST
//     @Path("/upload")
//     @Consumes(MediaType.MULTIPART_FORM_DATA)
//     public Response uploadFile(@MultipartForm FileUploadForm form) {
//         try {
//             String fileName = form.getFile().getFileName();
//             Path destination = Path.of("src/main/resources/uploads", fileName);
//             Files.copy(form.getFile().getData(), destination, StandardCopyOption.REPLACE_EXISTING);
//             return Response.ok("File uploaded successfully").build();
//         } catch (IOException e) {
//             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//         }
//     }
}
