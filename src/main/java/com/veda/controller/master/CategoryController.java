package com.veda.controller.master;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;

import com.veda.config.EntityCopyUtils;
import com.veda.entity.master.Category;
import com.veda.repository.master.CategoryRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/api/category")
@RolesAllowed("Admin")
@Tag(name = "Category", description = "Category Endpoints")
public class CategoryController {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    EntityCopyUtils entityCopyUtils;

    @GET
    public Response getAll(
            @DefaultValue("0") @QueryParam("pageNo") int pageNo,
            @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        Iterable<Category> categories = categoryRepository.findAll(PageRequest.of(pageNo, pageSize));
        return Response.ok(categories).status(200).build();
    }



}