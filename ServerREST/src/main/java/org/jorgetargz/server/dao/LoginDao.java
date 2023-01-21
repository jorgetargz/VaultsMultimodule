package org.jorgetargz.server.dao;

import org.jorgetargz.utils.modelo.Login;

public interface LoginDao {

    Login get(String username);

    Login save(Login login);

}
