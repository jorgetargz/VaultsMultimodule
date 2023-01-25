package org.jorgetargz.client.domain.services.impl;

import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import org.jorgetargz.client.dao.MessagesDAO;
import org.jorgetargz.client.domain.services.MessagesServices;
import org.jorgetargz.security.Encriptacion;
import org.jorgetargz.utils.modelo.ContentCiphed;
import org.jorgetargz.utils.modelo.Message;

import java.util.Base64;
import java.util.List;

public class MessagesServicesImpl implements MessagesServices {

    private final MessagesDAO messagesDAO;
    private final Encriptacion encriptacion;

    @Inject
    public MessagesServicesImpl(MessagesDAO messagesDAO, Encriptacion encriptacion) {
        this.messagesDAO = messagesDAO;
        this.encriptacion = encriptacion;
    }

    @Override
    public Single<Either<String, List<Message>>> getAll(String vaultName, String username, String password) {
        vaultName = Base64.getEncoder().encodeToString(vaultName.getBytes());
        username = Base64.getEncoder().encodeToString(username.getBytes());
        String passwordEncoded = Base64.getEncoder().encodeToString(password.getBytes());
        return messagesDAO.getAll(vaultName, username, passwordEncoded).map(either -> either.map(messages -> {
            messages.forEach(message ->
                    message.setContentUnsecured(encriptacion.desencriptar(message.getContentCiphed(), password)));
            return messages;
        }));
    }

    @Override
    public Single<Either<String, Message>> save(Message message, String password) {
        ContentCiphed contentCiphed = encriptacion.encriptar(message.getContentUnsecured(), password);
        password = Base64.getEncoder().encodeToString(password.getBytes());
        Message messageToSave = Message.builder()
                .idVault(message.getIdVault())
                .contentCiphed(contentCiphed)
                .build();
        return messagesDAO.save(messageToSave, password);
    }

    @Override
    public Single<Either<String, Message>> update(Message message, String password) {
        ContentCiphed contentCiphed = encriptacion.encriptar(message.getContentUnsecured(), password);
        password = Base64.getEncoder().encodeToString(password.getBytes());
        Message messageToUpdate = Message.builder()
                .id(message.getId())
                .idVault(message.getIdVault())
                .contentCiphed(contentCiphed)
                .build();
        return messagesDAO.update(messageToUpdate, password);
    }

    @Override
    public Single<Either<String, Boolean>> delete(int messageId) {
        return messagesDAO.delete(messageId);
    }
}
