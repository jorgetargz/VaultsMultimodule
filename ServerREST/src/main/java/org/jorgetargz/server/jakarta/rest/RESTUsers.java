package org.jorgetargz.server.jakarta.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.jorgetargz.server.domain.services.ServicesUsers;
import org.jorgetargz.utils.common.ConstantesAPI;
import org.jorgetargz.utils.modelo.User;

@Path(ConstantesAPI.PATH_USERS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RESTUsers {

    private final ServicesUsers servicesUsers;

    @Context
    SecurityContext securityContext;

    @Inject
    public RESTUsers(ServicesUsers servicesUsers) {
        this.servicesUsers = servicesUsers;
    }

    @POST
    @RolesAllowed(ConstantesAPI.ROLE_ADMIN)
    public Response register(User user) {
        return Response.status(Response.Status.CREATED)
                .entity(servicesUsers.scSave(user))
                .build();
    }

    @DELETE
    @RolesAllowed(ConstantesAPI.ROLE_ADMIN)
    public Response delete(User user) {
        servicesUsers.scDelete(user.getUsername());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}