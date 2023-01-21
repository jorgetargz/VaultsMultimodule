package org.jorgetargz.client.domain.services;

import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Either;
import org.jorgetargz.utils.modelo.Login;

public interface LoginServices {

    Single<Either<String, Login>> scGet(String username, String password);

    Single<Either<String, Boolean>> scLogout();

    Single<Either<String, Login>> scRegisterUser(Login login);

}
