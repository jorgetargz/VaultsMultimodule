package org.jorgetargz.client.dao;

import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Either;
import org.jorgetargz.utils.modelo.Login;


public interface LoginDAO {

    Single<Either<String, Login>> getReaderByLogin(String authorization);

    Single<Either<String, Boolean>> logout(String authorization);

    Single<Either<String, Login>> registerReader(Login login);
}
