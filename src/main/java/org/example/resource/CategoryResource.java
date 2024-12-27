package org.example.resource;

import org.example.model.Category;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class CategoryResource {

    @GET
    public List<Category> getAll() {
        return Category.listAll();
    }

    @GET
    @Path("/{id}")
    public Category getSingle(@PathParam("id") Long id) {
        return Category.findById(id);
    }

    @POST
    public Response create(Category category) {
        category.persist();
        return Response.status(Response.Status.CREATED).entity(category).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Category updatedCategory) {
        Category category = Category.findById(id);
        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        category.setName(updatedCategory.getName());
        category.persist();
        return Response.ok(category).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Category.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}