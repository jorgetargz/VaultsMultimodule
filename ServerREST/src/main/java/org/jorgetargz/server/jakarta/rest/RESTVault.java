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
import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.Vault;
import org.jorgetargz.utils.modelo.VaultCredential;

import java.util.List;

@Path(ConstantesAPI.PATH_VAULT)
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

    @POST
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    @Path(ConstantesAPI.VAULT_CHANGE_PASSWORD_PATH)
    public Vault changePassword(VaultCredential vaultCredential, @QueryParam("newPassword") String newPassword) {
        return servicesVaults.changePassword(vaultCredential, newPassword);
    }

    @POST
    @Path(ConstantesAPI.VAULT_MESSAGES_PATH)
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    public List<Message> getMessages(VaultCredential vaultCredential) {
        return servicesVaults.getMessages(vaultCredential);
    }

    @POST
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    @Path(ConstantesAPI.MESSAGE_CREATE_PATH)
    public Message createMessage(VaultCredential vaultCredential, @QueryParam(ConstantesAPI.MESSAGE_PARAM) String message) {
        return servicesVaults.createMessage(vaultCredential, message);
    }

    @PUT
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    @Path(ConstantesAPI.MESSAGE_UPDATE_PATH)
    public Message updateMessage(VaultCredential vaultCredential, @QueryParam(ConstantesAPI.MESSAGE_PARAM) String message, @QueryParam(ConstantesAPI.MESSAGE_ID_PARAM) int messageId) {
        return servicesVaults.updateMessage(vaultCredential, message, messageId);
    }

    @POST
    @Path(ConstantesAPI.MESSAGE_DELETE_PATH)
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    public Response deleteMessage(VaultCredential vaultCredential, @QueryParam(ConstantesAPI.MESSAGE_ID_PARAM) int messageId) {
        Message message = servicesVaults.deleteMessage(vaultCredential, messageId);
        if (message != null) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}