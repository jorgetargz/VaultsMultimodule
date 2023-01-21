package org.jorgetargz.server.domain.services;


import org.jorgetargz.utils.modelo.Login;

public interface ServicesLogin {

    Login scGet(String username);

    void scSave(Login login);

    boolean scCheckCredentials(String username, char[] password);

    void scLogout(String authorization);
}
