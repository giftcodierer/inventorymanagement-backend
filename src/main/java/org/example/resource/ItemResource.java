package org.example.resource;

import org.example.model.Item;
import org.example.model.User;
import org.example.requests.BorrowItemsRequest;
import org.example.requests.ItemIdsRequest;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

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

    @GET
    @Path("/borrowed")
    public Response getBorrowedItems(@QueryParam("userId") Long userId) {
        User user = User.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        List<Item> borrowedItems = Item.list("borrowedBy", user);
        return Response.ok(borrowedItems).build();
    }

    @POST
    public Response create(Item item, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }
        if (item.getId() != null) {
            item = Item.getEntityManager().merge(item);
        } else {
            item.persist();
        }
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Item updatedItem, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }
        Item item = Item.findById(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        item.setDeviceName(updatedItem.getDeviceName());
        item.setDeviceCondition(updatedItem.getDeviceCondition());
        item.setBorrowDuration(updatedItem.getBorrowDuration());
        item.setCategory(updatedItem.getCategory());
        item.setDepartment(updatedItem.getDepartment());
        item.persist();
        return Response.ok(item).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }
        boolean deleted = Item.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/borrow-multiple")
    public Response borrowMultipleItems(BorrowItemsRequest request, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        User user = User.find("username", username).firstResult();
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }

        List<Long> itemIds = request.getItemIds();
        Integer borrowDuration = request.getBorrowDuration();

        for (Long itemId : itemIds) {
            Item item = Item.findById(itemId);
            if (item == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Item not found: " + itemId).build();
            }
            item.setBorrowedBy(user);
            item.setBorrowDuration(borrowDuration);

            // Calculate the borrow end date
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, borrowDuration);
            item.setBorrowedUntil(calendar.getTime());

            item.persist();
        }

        // Return a JSON response
        return Response.ok("{\"message\": \"Items successfully borrowed\"}").build();
    }
    @POST
    @Path("/by-ids")
    public Response getItemsByIds(ItemIdsRequest request) {
        List<Long> ids = request.getIds();
        List<Item> items = ids.stream()
                .map(id -> (Item) Item.findById(id))
                .collect(Collectors.toList());
        return Response.ok(items).build();
    }

    @POST
    @Path("/return/{id}")
    public Response returnItem(@PathParam("id") Long itemId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        User user = User.find("username", username).firstResult();
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }

        Item item = Item.findById(itemId);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Item not found: " + itemId).build();
        }
        if (!item.getBorrowedBy().equals(user)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Item not borrowed by user: " + itemId).build();
        }

        // Revert the borrowing changes
        item.setBorrowedBy(null);
        item.setBorrowDuration(null);
        item.setBorrowedUntil(null);

        item.persist();

        return Response.ok("{\"message\": \"Item successfully returned\"}").build();
    }
}