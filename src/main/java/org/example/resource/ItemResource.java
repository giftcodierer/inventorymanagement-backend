package org.example.resource;

import org.example.model.Item;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class ItemResource {

    @GET
    public List<Item> getAll() {
        return Item.listAll();
    }

    @GET
    @Path("/{id}")
    public Item getSingle(@PathParam("id") Long id) {
        return Item.findById(id);
    }

    @POST
    public Response create(Item item) {
        item.persist();
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Item updatedItem) {
        Item item = Item.findById(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        item.setName(updatedItem.getName());
        item.setCategory(updatedItem.getCategory());
        item.setDepartment(updatedItem.getDepartment());
        item.persist();
        return Response.ok(item).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Item.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
