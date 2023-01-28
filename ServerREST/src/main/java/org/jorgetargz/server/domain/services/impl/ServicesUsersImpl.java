package org.jorgetargz.server.domain.services.impl;


import org.jorgetargz.server.dao.UsersDao;
import org.jorgetargz.server.dao.excepciones.UnauthorizedException;
import org.jorgetargz.server.domain.common.Constantes;
import org.jorgetargz.server.domain.services.ServicesUsers;
import org.jorgetargz.server.domain.services.excepciones.ValidationException;
import jakarta.inject.Inject;
import org.jorgetargz.server.jakarta.security.JWTBlackList;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.extern.log4j.Log4j2;
import org.jorgetargz.utils.modelo.User;

import java.io.Serializable;
import java.util.Base64;

@Log4j2
public class ServicesUsersImpl implements ServicesUsers, Serializable {

    private final UsersDao daoLogin;
    private final JWTBlackList jwtBlackList;
    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public ServicesUsersImpl(UsersDao daoLogin, JWTBlackList jwtBlackList, Pbkdf2PasswordHash passwordHash) {
        this.daoLogin = daoLogin;
        this.jwtBlackList = jwtBlackList;
        this.passwordHash = passwordHash;
    }

    @Override
    public User scGet(String username) {
        if (username == null) {
            log.warn(Constantes.USERNAME_EMPTY);
            throw new ValidationException(Constantes.USERNAME_OR_PASSWORD_EMPTY);
        }
        return daoLogin.get(username);
    }

    @Override
    public User scSave(User user) {
        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        return daoLogin.save(user);
    }

    @Override
    public User scCheckCredentials(String username, char[] password) throws UnauthorizedException, ValidationException {
        if (username == null || password == null) {
            log.warn(Constantes.USERNAME_OR_PASSWORD_EMPTY);
            throw new ValidationException(Constantes.USERNAME_OR_PASSWORD_EMPTY);
        }
        User userDB = daoLogin.get(username);
        if (!passwordHash.verify(password, userDB.getPassword())) {
            log.warn(Constantes.USERNAME_OR_PASSWORD_INCORRECT);
            throw new UnauthorizedException(Constantes.USERNAME_OR_PASSWORD_INCORRECT);
        }
        return userDB;
    }

    @Override
    public void scLogout(String authorization) {
        String[] headerFields = authorization.split(Constantes.WHITE_SPACE);
        if (headerFields.length == 2) {
            String token = headerFields[1];
            jwtBlackList.getJWTBlackList().add(token);
        }
    }

    @Override
    public void scDelete(String username) {
        username = new String(Base64.getDecoder().decode(username));
        daoLogin.delete(username);
    }

}