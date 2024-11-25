package org.example.resource;

import org.example.model.Department;
import org.example.service.DepartmentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    @Inject
    DepartmentService departmentService;

    @POST
    public Response createDepartment(Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return Response.status(Response.Status.CREATED).entity(createdDepartment).build();
    }

    @GET
    @Path("/{id}")
    public Response getDepartmentById(@PathParam("id") Long id) {
        Department department = departmentService.getDepartmentById(id);
        if (department != null) {
            return Response.ok(department).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PUT
    @Path("/{id}")
    public Response updateDepartment(@PathParam("id") Long id, Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        if (updatedDepartment != null) {
            return Response.ok(updatedDepartment).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDepartment(@PathParam("id") Long id) {
        departmentService.deleteDepartment(id);
        return Response.noContent().build();
    }
}