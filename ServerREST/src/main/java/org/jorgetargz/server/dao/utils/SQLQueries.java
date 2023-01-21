package org.jorgetargz.server.dao.utils;

public class SQLQueries {


    public static final String INSERT_LOGIN_QUERY = "INSERT INTO login (username, password, role) VALUES (?, ?, ?)";
    public static final String SELECT_LOGIN_BY_USERNAME_QUERY = "SELECT * FROM login WHERE username = ?";
    public static final String SELECT_MESSAGES_QUERY = "SELECT * FROM messages WHERE vaultId in (Select id FROM vaults WHERE name = ? AND username = ?)";
    public static final String SELECT_VAULT = "SELECT * FROM vaults WHERE name = ? AND username = ?";
    public static final String INSERT_VAULT_QUERY = "INSERT INTO vaults (name, username, password, `read`, `write`) VALUES (?, ?, ?, ?, ?)";
    public static final String INSERT_MESSAGE_QUERY = "INSERT INTO messages (vaultId, message) VALUES (?, ?)";
    public static final String UPDATE_MESSAGE_QUERY = "UPDATE messages SET message = ? WHERE id = ?";
    public static final String DELETE_MESSAGE_QUERY = "DELETE FROM messages WHERE id = ?";
    public static final String UPDATE_VAULT_PASSWORD_QUERY = "UPDATE vaults SET password = ? WHERE id = ? AND username = ?";
    public static final String GET_VAULTS = "select * from vaults where username = ?";

    private SQLQueries() {
    }

}