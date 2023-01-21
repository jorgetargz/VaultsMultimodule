package org.jorgetargz.client.dao.impl;

import com.google.gson.Gson;
import org.jorgetargz.client.dao.LoginDAO;
import org.jorgetargz.client.dao.vault_api.LoginAPI;
import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import org.jorgetargz.utils.modelo.Login;

public class LoginDAOImpl extends GenericDAO implements LoginDAO {

    private final LoginAPI loginAPI;

    @Inject
    public LoginDAOImpl(Gson gson, LoginAPI loginAPI) {
        super(gson);
        this.loginAPI = loginAPI;
    }

    @Override
    public Single<Either<String, Login>> getReaderByLogin(String authorization) {
        return safeAPICall(loginAPI.getReaderByLogin(authorization));
    }

    @Override
    public Single<Either<String, Boolean>> logout(String authorization) {
        return safeAPICallResponseVoid(loginAPI.logout(authorization));
    }

    @Override
    public Single<Either<String, Login>> registerReader(Login login) {
        return safeAPICall(loginAPI.registerReader(login));
    }
}
