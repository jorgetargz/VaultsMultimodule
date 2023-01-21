package org.jorgetargz.server.dao.jdbc_impl;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.jorgetargz.server.dao.DBConnection;
import org.jorgetargz.server.dao.VaultsDao;
import org.jorgetargz.server.dao.common.Constantes;
import org.jorgetargz.server.dao.excepciones.DatabaseException;
import org.jorgetargz.server.dao.excepciones.NotFoundException;
import org.jorgetargz.server.dao.utils.SQLQueries;
import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.Vault;
import org.jorgetargz.utils.modelo.VaultCredential;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class VaultsDaoImpl implements VaultsDao {

    private final DBConnection dbConnection;

    @Inject
    public VaultsDaoImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Vault> getVaults(String username) {
        List<Vault> vaults = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.GET_VAULTS)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Vault vault = new Vault();
                vault.setId(resultSet.getInt(Constantes.ID));
                vault.setName(resultSet.getString(Constantes.NAME));
                vault.setUsername(resultSet.getString(Constantes.USERNAME));
                vault.setPassword(resultSet.getString(Constantes.PASSWORD));
                vault.setReadByAll(resultSet.getBoolean(Constantes.READ_BY_ALL));
                vault.setWriteByAll(resultSet.getBoolean(Constantes.WRITE_BY_ALL));
                vaults.add(vault);
            }
            if (vaults.isEmpty()) {
                throw new NotFoundException("No vaults found or wrong username/password");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
        return vaults;
    }

    @Override
    public Vault getVault(VaultCredential vaultCredential) {
        try (Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SELECT_VAULT)) {
            preparedStatement.setString(1, vaultCredential.getVaultName());
            preparedStatement.setString(2, vaultCredential.getUsernameOwner());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Vault vault = new Vault();
                vault.setId(resultSet.getInt("id"));
                vault.setName(resultSet.getString("name"));
                vault.setUsername(resultSet.getString("username"));
                vault.setPassword(resultSet.getString("password"));
                vault.setReadByAll(resultSet.getInt("read") == 1);
                vault.setWriteByAll(resultSet.getInt("write") == 1);
                return vault;
            } else {
                throw new NotFoundException("Vault not found");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    @Override
    public List<Message> getMessages(VaultCredential vaultCredential) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SELECT_MESSAGES_QUERY)) {
            preparedStatement.setString(1, vaultCredential.getVaultName());
            preparedStatement.setString(2, vaultCredential.getUsernameOwner());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Message> messages = new ArrayList<>();
            if (resultSet.next()) {
                Message message = new Message();
                message.setId(resultSet.getInt("id"));
                message.setIdVault(resultSet.getInt("vaultId"));
                message.setMessage(resultSet.getString("message"));
                messages.add(message);
            } else {
                log.warn(Constantes.MESSAGE_NOT_FOUND);
                throw new NotFoundException(Constantes.MESSAGE_NOT_FOUND);
            }
            return messages;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    @Override
    public Vault createVault(Vault vault) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.INSERT_VAULT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, vault.getName());
            preparedStatement.setString(2, vault.getUsername());
            preparedStatement.setString(3, vault.getPassword());
            preparedStatement.setInt(4, vault.isReadByAll() ? 1 : 0);
            preparedStatement.setInt(5, vault.isWriteByAll() ? 1 : 0);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                vault.setId(resultSet.getInt(1));
                return vault;
            } else {
                throw new DatabaseException(Constantes.DATABASE_ERROR);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    @Override
    public Message createMessage(VaultCredential vaultCredential, String message) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatementGetVault = connection.prepareStatement(SQLQueries.SELECT_VAULT);
             PreparedStatement preparedStatementInsertMessage = connection.prepareStatement(SQLQueries.INSERT_MESSAGE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatementGetVault.setString(1, vaultCredential.getVaultName());
            preparedStatementGetVault.setString(2, vaultCredential.getUsernameOwner());
            ResultSet resultSet = preparedStatementGetVault.executeQuery();
            if (resultSet.next()) {
                preparedStatementInsertMessage.setInt(1, resultSet.getInt("id"));
                preparedStatementInsertMessage.setString(2, message);
                preparedStatementInsertMessage.executeUpdate();
                ResultSet resultSetMessage = preparedStatementInsertMessage.getGeneratedKeys();
                if (resultSetMessage.next()) {
                    Message message1 = new Message();
                    message1.setId(resultSetMessage.getInt(1));
                    message1.setIdVault(resultSet.getInt("id"));
                    message1.setMessage(message);
                    return message1;
                } else {
                    throw new DatabaseException(Constantes.DATABASE_ERROR);
                }
            } else {
                throw new NotFoundException(Constantes.VAULT_NOT_FOUND);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    @Override
    public Message updateMessage(VaultCredential vaultCredential, String message, int messageId) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatementGetVault = connection.prepareStatement(SQLQueries.SELECT_VAULT);
             PreparedStatement preparedStatementUpdateMessage = connection.prepareStatement(SQLQueries.UPDATE_MESSAGE_QUERY)) {

            preparedStatementGetVault.setString(1, vaultCredential.getVaultName());
            preparedStatementGetVault.setString(2, vaultCredential.getUsernameOwner());
            ResultSet resultSetVault = preparedStatementGetVault.executeQuery();
            if (resultSetVault.next()) {
                preparedStatementUpdateMessage.setString(1, message);
                preparedStatementUpdateMessage.setInt(2, messageId);
                if (preparedStatementUpdateMessage.executeUpdate() == 1) {
                    Message message1 = new Message();
                    message1.setId(messageId);
                    message1.setIdVault(resultSetVault.getInt("id"));
                    message1.setMessage(message);
                    return message1;
                } else {
                    throw new NotFoundException("Message not found");
                }
            } else {
                throw new NotFoundException("Vault not found");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    @Override
    public Message deleteMessage(VaultCredential vaultCredential, int messageId) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatementDeleteMessage = connection.prepareStatement(SQLQueries.DELETE_MESSAGE_QUERY)) {

            preparedStatementDeleteMessage.setInt(1, messageId);
            if (preparedStatementDeleteMessage.executeUpdate() == 1) {
                Message message1 = new Message();
                message1.setId(messageId);
                return message1;
            } else {
                throw new NotFoundException("Message not found");
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    @Override
    public Vault changePassword(Vault vault, String newPassword) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.UPDATE_VAULT_PASSWORD_QUERY)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, vault.getId());
            preparedStatement.setString(3, vault.getUsername());
            if (preparedStatement.executeUpdate() == 1) {
                vault.setPassword(newPassword);
                return vault;
            } else {
                throw new NotFoundException("Vault not found");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }


}