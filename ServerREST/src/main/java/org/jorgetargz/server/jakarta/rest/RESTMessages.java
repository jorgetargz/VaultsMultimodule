package org.jorgetargz.server.jakarta.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.jorgetargz.server.domain.services.ServicesMessages;
import org.jorgetargz.utils.common.ConstantesAPI;
import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.Vault;

import java.util.List;

@Path(ConstantesAPI.PATH_MESSAGES)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RESTMessages {

    private final ServicesMessages servicesMessages;

    @Context
    SecurityContext securityContext;

    @Inject
    public RESTMessages(ServicesMessages servicesMessages) {
        this.servicesMessages = servicesMessages;
    }

    @GET
    @Path(ConstantesAPI.VAULT_PATH + ConstantesAPI.VAULT_ID_PATH_PARAM)
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    public List<Message> getMessages(@PathParam(ConstantesAPI.VAULT_ID_PARAM) int vaultId, @QueryParam("vaultName") String vaultName, @QueryParam("usernameOwner") String usernameOwner, @QueryParam("password") String password) {
        Vault credentials = Vault.builder()
                .id(vaultId)
                .name(vaultName)
                .usernameOwner(usernameOwner)
                .password(password)
                .build();
        return servicesMessages.getMessages(credentials, securityContext.getUserPrincipal().getName());
    }

    @POST
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    public Message createMessage(Message message, @QueryParam("password") String password) {
        return servicesMessages.createMessage(message, password, securityContext.getUserPrincipal().getName());
    }

    @PUT
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    public Message updateMessage(Message message, @QueryParam("password") String password) {
        return servicesMessages.updateMessage(message, password, securityContext.getUserPrincipal().getName());
    }

    @DELETE
    @Path(ConstantesAPI.MESSAGE_DELETE_PATH + ConstantesAPI.MESSAGE_ID_PATH_PARAM)
    @RolesAllowed(ConstantesAPI.ROLE_USER)
    public Response deleteMessage(@PathParam(ConstantesAPI.MESSAGE_ID_PARAM) int messageId) {
        servicesMessages.deleteMessage(messageId, securityContext.getUserPrincipal().getName());
        return Response.noContent().build();
    }
}
