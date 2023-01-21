package org.jorgetargz.server.jakarta.rest;

import jakarta.annotation.security.RolesAllowed;
import org.jorgetargz.utils.common.ConstantesAPI;
import org.jorgetargz.server.domain.services.ServicesLogin;
import org.jorgetargz.server.jakarta.common.Constantes;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jorgetargz.utils.modelo.BaseError;
import org.jorgetargz.utils.modelo.Login;

import java.time.LocalDateTime;

@Path(ConstantesAPI.PATH_LOGIN)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RESTLogin {

    private final ServicesLogin servicesLogin;

    @Context
    private HttpServletRequest httpRequest;

    @Context
    SecurityContext securityContext;

    @Inject
    public RESTLogin(ServicesLogin servicesLogin) {
        this.servicesLogin = servicesLogin;
    }

    @POST
    @RolesAllowed(ConstantesAPI.ROLE_ADMIN)
    public Response register(Login login) {
        servicesLogin.scSave(login);
        return Response.status(Response.Status.CREATED)
                .entity(login)
                .build();
    }

    @GET
    public Response login() {
        if (securityContext.getUserPrincipal() != null) {
            return Response.ok()
                    .entity(servicesLogin.scGet(securityContext.getUserPrincipal().getName()))
                    .build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new BaseError((String) httpRequest.getAttribute(Constantes.ERROR_LOGIN), LocalDateTime.now()))
                    .build();
        }
    }

    @GET
    @Path(ConstantesAPI.LOGOUT_PATH)
    public Response logout(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization) {
        if (securityContext.getUserPrincipal() != null) {
            servicesLogin.scLogout(authorization);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new BaseError((String) httpRequest.getAttribute(Constantes.ERROR_LOGIN), LocalDateTime.now()))
                    .build();
        }
    }

}