package org.jorgetargz.server.domain.services.impl;

import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.jorgetargz.server.dao.MessagesDao;
import org.jorgetargz.server.dao.VaultsDao;
import org.jorgetargz.server.domain.services.ServicesMessages;
import org.jorgetargz.server.domain.services.excepciones.ValidationException;
import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.Vault;

import java.util.List;

public class ServicesMessagesImpl implements ServicesMessages {

    private final MessagesDao messageDao;
    private final VaultsDao vaultsDao;
    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public ServicesMessagesImpl(MessagesDao messageDao, VaultsDao vaultsDao, Pbkdf2PasswordHash passwordHash) {
        this.messageDao = messageDao;
        this.vaultsDao = vaultsDao;
        this.passwordHash = passwordHash;
    }

    @Override
    public List<Message> getMessages(Vault credentials, String usernameReader) {
        Vault vault = vaultsDao.getVault(credentials.getId());
        if (vault.getName().equals(credentials.getName())
                && vault.getUsernameOwner().equals(credentials.getUsernameOwner())
                && passwordHash.verify(credentials.getPassword().toCharArray(), vault.getPassword())) {
            if (vault.getUsernameOwner().equals(usernameReader) || vault.isReadByAll()) {
                return messageDao.getMessages(credentials.getId());
            } else {
                throw new ValidationException("You don't have permission to read this vault");
            }
        } else {
            throw new ValidationException("Wrong credentials");
        }
    }

    @Override
    public Message createMessage(Message message, String password, String usernameReader) {
        int vaultId = message.getIdVault();
        Vault vault = vaultsDao.getVault(vaultId);
        checkPermsionToWrite(vault, password, usernameReader);
        return messageDao.createMessage(vaultId, message);
    }

    @Override
    public Message updateMessage(Message message, String password, String usernameReader) {
        checkPermsionToWrite(vaultsDao.getVault(message.getIdVault()), password, usernameReader);
        return messageDao.updateMessage(message.getId(), message);
    }

    private void checkPermsionToWrite(Vault vault, String password, String usernameReader) {
        if (passwordHash.verify(password.toCharArray(), vault.getPassword())) {
            if (!vault.getUsernameOwner().equals(usernameReader) && !vault.isWriteByAll()) {
                throw new ValidationException("Only the owner of the vault can write in it");
            }
        } else {
            throw new ValidationException("Wrong password");
        }
    }

    @Override
    public void deleteMessage(int messageId, String usernameReader) {
        Vault vault = messageDao.getVault(messageId);
        if (vault.getUsernameOwner().equals(usernameReader)) {
            messageDao.deleteMessage(messageId);
        } else {
            throw new ValidationException("You don't have permission to write in this vault");
        }
    }
}
