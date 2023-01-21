package org.jorgetargz.server.domain.services.impl;


import org.jorgetargz.server.dao.LoginDao;
import org.jorgetargz.server.domain.common.Constantes;
import org.jorgetargz.server.domain.services.ServicesLogin;
import org.jorgetargz.server.domain.services.excepciones.ValidationException;
import jakarta.inject.Inject;
import org.jorgetargz.server.jakarta.security.JWTBlackList;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.extern.log4j.Log4j2;
import org.jorgetargz.utils.modelo.Login;

import java.io.Serializable;

@Log4j2
public class ServicesLoginImpl implements ServicesLogin, Serializable {

    private final LoginDao daoLogin;
    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public ServicesLoginImpl(LoginDao daoLogin, Pbkdf2PasswordHash passwordHash) {
        this.daoLogin = daoLogin;
        this.passwordHash = passwordHash;
    }

    @Override
    public Login scGet(String username) {
        if (username == null) {
            log.warn(Constantes.USERNAME_EMPTY);
            throw new ValidationException(Constantes.USERNAME_OR_PASSWORD_EMPTY);
        }
        return daoLogin.get(username);
    }

    @Override
    public void scSave(Login login) {
        login.setPassword(passwordHash.generate(login.getPassword().toCharArray()));
        daoLogin.save(login);
    }

    @Override
    public boolean scCheckCredentials(String username, char[] password) {
        if (username == null || password == null) {
            log.warn(Constantes.USERNAME_OR_PASSWORD_EMPTY);
            throw new ValidationException(Constantes.USERNAME_OR_PASSWORD_EMPTY);
        }
        Login loginDB = daoLogin.get(username);
        if (!passwordHash.verify(password, loginDB.getPassword())) {
            log.warn(Constantes.USERNAME_OR_PASSWORD_INCORRECT);
            throw new ValidationException(Constantes.USERNAME_OR_PASSWORD_INCORRECT);
        } else {
            return true;
        }
    }

    @Override
    public void scLogout(String authorization) {
        String[] headerFields = authorization.split(Constantes.WHITE_SPACE);
        if (headerFields.length == 2) {
            String token = headerFields[1];
            JWTBlackList.getInstance().addToken(token);
        }
    }

}