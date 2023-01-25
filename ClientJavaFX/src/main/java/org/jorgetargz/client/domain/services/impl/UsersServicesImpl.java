package org.jorgetargz.client.domain.services.impl;

import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import org.jorgetargz.client.dao.UsersDAO;
import org.jorgetargz.client.domain.services.UsersServices;
import org.jorgetargz.utils.common.ConstantesAPI;
import org.jorgetargz.utils.modelo.User;

import java.util.Base64;

public class UsersServicesImpl implements UsersServices {

    private final UsersDAO usersDAO;

    @Inject
    public UsersServicesImpl(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    @Override
    public Single<Either<String, User>> save(User user) {
        user.setRole(ConstantesAPI.ROLE_USER);
        return usersDAO.save(user);
    }

    @Override
    public Single<Either<String, Boolean>> delete(String username) {
        username = Base64.getEncoder().encodeToString(username.getBytes());
        return usersDAO.delete(username);
    }
}
