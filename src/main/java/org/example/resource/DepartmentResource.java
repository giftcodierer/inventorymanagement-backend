package org.example.resource;

import org.example.model.Department;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class DepartmentResource {

    @GET
    public List<Department> getAll() {
        return Department.listAll();
    }

    @GET
    @Path("/{id}")
    public Department getSingle(@PathParam("id") Long id) {
        return Department.findById(id);
    }

    @POST
    public Response create(Department department) {
        if (department.getId() != null) {
            department = Department.getEntityManager().merge(department);
        } else {
            department.persist();
        }
        return Response.status(Response.Status.CREATED).entity(department).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Department updatedDepartment) {
        Department department = Department.findById(id);
        if (department == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        department.setName(updatedDepartment.getName());
        department.setLocation(updatedDepartment.getLocation()); // Update location
        department.persist();
        return Response.ok(department).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Department.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}