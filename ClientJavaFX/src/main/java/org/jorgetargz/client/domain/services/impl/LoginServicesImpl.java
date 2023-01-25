package org.jorgetargz.client.domain.services.impl;

import org.jorgetargz.client.dao.LoginDAO;
import org.jorgetargz.client.domain.services.LoginServices;
import org.jorgetargz.client.dao.vault_api.utils.CacheAuthorization;
import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import org.jorgetargz.utils.modelo.User;
import okhttp3.Credentials;

public class LoginServicesImpl implements LoginServices {

    private final LoginDAO loginDAO;
    private final CacheAuthorization cache;

    @Inject
    public LoginServicesImpl(LoginDAO loginDAO, CacheAuthorization cache) {
        this.loginDAO = loginDAO;
        this.cache = cache;
    }

    @Override
    public Single<Either<String, User>> scGet(String username, String password) {
        cache.setUser(username);
        cache.setPassword(password);
        return loginDAO.getReaderByLogin(Credentials.basic(username, password));
    }

    @Override
    public Single<Either<String, Boolean>> scLogout() {
        String jwtAuth = cache.getJwtAuth();
        cache.setUser(null);
        cache.setPassword(null);
        cache.setJwtAuth(null);
        return loginDAO.logout(jwtAuth);
    }

    @Override
    public Single<Either<String, User>> scRegisterUser(User user) {
        return loginDAO.registerReader(user);
    }
}
