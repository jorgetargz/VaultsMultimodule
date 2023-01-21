package org.jorgetargz.server.jakarta.rest;


import org.jorgetargz.utils.common.ConstantesAPI;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath(ConstantesAPI.API_PATH)
@DeclareRoles({ConstantesAPI.ROLE_ADMIN, ConstantesAPI.ROLE_USER})
public class JAXRSApplication extends Application {
}
