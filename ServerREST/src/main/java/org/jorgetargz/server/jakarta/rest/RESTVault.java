package org.jorgetargz.server.jakarta.rest;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.jorgetargz.server.domain.services.ServicesVaults;
import org.jorgetargz.utils.common.ConstantesAPI;
import org.jorgetargz.utils.modelo.Vault;

import java.util.List;

@Path(ConstantesAPI.PATH_VAULTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RESTVault {

    private final ServicesVaults servicesVaults;

    @Context
    SecurityContext securityContext;

    @Inject
    public RESTVault(ServicesVaults servicesVaults) {
        this.servicesVaults = servicesVaults;
    }

    @GET
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    public List<Vault> getVaults() {
        return servicesVaults.getVaults(securityContext.getUserPrincipal().getName());
    }

    @POST
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    public Vault createVault(Vault vault) {
        return servicesVaults.createVault(vault);
    }

    @PATCH
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    @Path(ConstantesAPI.VAULT_CHANGE_PASSWORD_PATH)
    public Response changePassword(Vault credentials, @QueryParam("password") String password) {
        servicesVaults.changePassword(credentials, password, securityContext.getUserPrincipal().getName());
        return Response.ok().build();
    }

    @DELETE
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    @Path(ConstantesAPI.VAULT_ID_PATH_PARAM)
    public Response deleteVault(@PathParam(ConstantesAPI.VAULT_ID_PARAM) int vaultId) {
        servicesVaults.deleteVault(vaultId, securityContext.getUserPrincipal().getName());
        return Response.noContent().build();
    }


}