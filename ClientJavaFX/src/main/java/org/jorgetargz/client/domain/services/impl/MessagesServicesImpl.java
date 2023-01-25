package org.jorgetargz.client.domain.services.impl;

import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import org.jorgetargz.client.dao.MessagesDAO;
import org.jorgetargz.client.domain.services.MessagesServices;
import org.jorgetargz.utils.modelo.Message;

import java.util.Base64;
import java.util.List;

public class MessagesServicesImpl implements MessagesServices {

    private final MessagesDAO messagesDAO;

    @Inject
    public MessagesServicesImpl(MessagesDAO messagesDAO) {
        this.messagesDAO = messagesDAO;
    }

    @Override
    public Single<Either<String, List<Message>>> getAll(int vaultId, String vaultName, String username, String password) {
        vaultName = Base64.getEncoder().encodeToString(vaultName.getBytes());
        username = Base64.getEncoder().encodeToString(username.getBytes());
        password = Base64.getEncoder().encodeToString(password.getBytes());
        return messagesDAO.getAll(vaultId, vaultName, username, password);
    }

    @Override
    public Single<Either<String, Message>> save(Message message, String password) {
        password = Base64.getEncoder().encodeToString(password.getBytes());
        return messagesDAO.save(message, password);
    }

    @Override
    public Single<Either<String, Message>> update(Message message, String password) {
        password = Base64.getEncoder().encodeToString(password.getBytes());
        return messagesDAO.update(message, password);
    }

    @Override
    public Single<Either<String, Boolean>> delete(int messageId) {
        return messagesDAO.delete(messageId);
    }
}
