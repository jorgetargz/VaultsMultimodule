package org.jorgetargz.server.dao.jdbc_impl;

import org.jorgetargz.server.dao.DBConnection;
import org.jorgetargz.server.dao.LoginDao;
import org.jorgetargz.server.dao.common.Constantes;
import org.jorgetargz.server.dao.excepciones.DatabaseException;
import org.jorgetargz.server.dao.excepciones.NotFoundException;
import org.jorgetargz.server.dao.utils.SQLQueries;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.jorgetargz.utils.modelo.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j2
public class LoginDaoImpl implements LoginDao {

    private final DBConnection dbConnection;

    @Inject
    public LoginDaoImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Login get(String username) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SELECT_LOGIN_BY_USERNAME_QUERY)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getLoginFromResultSet(resultSet);
            } else {
                log.warn(Constantes.LOGIN_NOT_FOUND);
                throw new NotFoundException(Constantes.LOGIN_NOT_FOUND);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    @Override
    public Login save(Login login) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.INSERT_LOGIN_QUERY)) {
            preparedStatement.setString(1, login.getUsername());
            preparedStatement.setString(2, login.getPassword());
            preparedStatement.setString(3, login.getRole());
            preparedStatement.executeUpdate();
            return login;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    private Login getLoginFromResultSet(ResultSet resultSet) throws SQLException {
        Login login = new Login();
        login.setUsername(resultSet.getString(Constantes.USERNAME));
        login.setPassword(resultSet.getString(Constantes.PASSWORD));
        login.setRole(resultSet.getString(Constantes.ROLE));
        return login;
    }

}