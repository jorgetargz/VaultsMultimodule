package org.jorgetargz.server.dao.jdbc_impl;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.jorgetargz.server.dao.DBConnection;
import org.jorgetargz.server.dao.MessagesDao;
import org.jorgetargz.server.dao.common.Constantes;
import org.jorgetargz.server.dao.excepciones.DatabaseException;
import org.jorgetargz.server.dao.excepciones.NotFoundException;
import org.jorgetargz.server.dao.utils.SQLQueries;
import org.jorgetargz.utils.modelo.ContentCiphed;
import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.Vault;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MessagesDaoImpl implements MessagesDao {

    private final DBConnection dbConnection;

    @Inject
    public MessagesDaoImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Vault getVault(int messageId) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SELECT_VAULT_BY_MESSAGE_ID)) {
            return getVault(messageId, preparedStatement);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    private Vault getVault(int messageId, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, messageId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Vault.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .usernameOwner(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .readByAll(resultSet.getInt("read") == 1)
                    .writeByAll(resultSet.getInt("write") == 1)
                    .build();
        } else {
            throw new NotFoundException("Vault not found");
        }
    }

    @Override
    public List<Message> getMessages(int vaultId) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SELECT_MESSAGES_QUERY)) {
            preparedStatement.setInt(1, vaultId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Message> messages = new ArrayList<>();
            if (resultSet.next()) {
                Message message = Message.builder()
                        .id(resultSet.getInt("id"))
                        .idVault(vaultId)
                        .contentCiphed(ContentCiphed.builder()
                                .iv(resultSet.getBytes("iv"))
                                .salt(resultSet.getBytes("salt"))
                                .cipherText(resultSet.getBytes("cipherText"))
                                .build())
                        .build();
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
    public Message createMessage(int vaultId, Message message) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatementInsertMessage = connection.prepareStatement(SQLQueries.INSERT_MESSAGE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatementInsertMessage.setInt(1, vaultId);
            preparedStatementInsertMessage.setBytes(2, message.getContentCiphed().getIv());
            preparedStatementInsertMessage.setBytes(3, message.getContentCiphed().getSalt());
            preparedStatementInsertMessage.setBytes(4, message.getContentCiphed().getCipherText());
            preparedStatementInsertMessage.executeUpdate();
            ResultSet resultSetMessage = preparedStatementInsertMessage.getGeneratedKeys();
            if (resultSetMessage.next()) {
                return Message.builder()
                        .id(resultSetMessage.getInt(1))
                        .idVault(vaultId)
                        .contentCiphed(message.getContentCiphed())
                        .build();
            } else {
                throw new DatabaseException(Constantes.DATABASE_ERROR);
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    @Override
    public Message updateMessage(int messageId, Message message) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatementUpdateMessage = connection.prepareStatement(SQLQueries.UPDATE_MESSAGE_QUERY)) {

            preparedStatementUpdateMessage.setBytes(1, message.getContentCiphed().getIv());
            preparedStatementUpdateMessage.setBytes(2, message.getContentCiphed().getSalt());
            preparedStatementUpdateMessage.setBytes(3, message.getContentCiphed().getCipherText());
            preparedStatementUpdateMessage.setInt(4, messageId);
            if (preparedStatementUpdateMessage.executeUpdate() == 1) {
                return Message.builder()
                        .id(messageId)
                        .contentCiphed(message.getContentCiphed())
                        .build();
            } else {
                throw new NotFoundException("Message not found");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteMessage(int messageId) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatementDeleteMessage = connection.prepareStatement(SQLQueries.DELETE_MESSAGE_QUERY)) {

            preparedStatementDeleteMessage.setInt(1, messageId);
            if (preparedStatementDeleteMessage.executeUpdate() != 1) {
                throw new NotFoundException("Message not found");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseException(Constantes.DATABASE_ERROR);
        }
    }

}