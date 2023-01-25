package org.jorgetargz.server.domain.services.impl;

import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.jorgetargz.security.Encriptacion;
import org.jorgetargz.server.dao.MessagesDao;
import org.jorgetargz.server.dao.VaultsDao;
import org.jorgetargz.server.domain.services.ServicesVaults;
import org.jorgetargz.server.domain.services.excepciones.ValidationException;
import org.jorgetargz.utils.modelo.*;

import java.util.List;

public class ServicesVaultsImpl implements ServicesVaults {

    private final VaultsDao vaultsDao;
    private final MessagesDao messageDao;
    private final Pbkdf2PasswordHash passwordHash;
    private final Encriptacion encriptacion;

    @Inject
    public ServicesVaultsImpl(VaultsDao vaultsDao, MessagesDao messageDao, Pbkdf2PasswordHash passwordHash, Encriptacion encriptacion) {
        this.vaultsDao = vaultsDao;
        this.messageDao = messageDao;
        this.passwordHash = passwordHash;
        this.encriptacion = encriptacion;
    }


    @Override
    public List<Vault> getVaults(String username) {
        return vaultsDao.getVaults(username);
    }

    @Override
    public Vault createVault(Vault vault) {
        vault.setPassword(passwordHash.generate(vault.getPassword().toCharArray()));
        return vaultsDao.createVault(vault);
    }

    @Override
    public void changePassword(Vault credentials, String password, String usernameReader) {
        int vaultId = credentials.getId();
        Vault vault = vaultsDao.getVault(vaultId);
        if (passwordHash.verify(credentials.getPassword().toCharArray(), vault.getPassword())
                && vault.getUsernameOwner().equals(usernameReader)) {
            List<Message> messages = messageDao.getMessages(vaultId);
            for (Message message : messages) {
                String messageText = encriptacion.desencriptar(message.getContentCiphed(), credentials.getPassword());
                ContentCiphed contentCiphed = encriptacion.encriptar(messageText, password);
                message.setContentCiphed(contentCiphed);
                messageDao.updateMessage(message.getId(), message);
            }
            password = passwordHash.generate(password.toCharArray());
            vaultsDao.changePassword(vaultId, password);
        } else {
            throw new ValidationException("Wrong password");
        }
    }

    @Override
    public void deleteVault(int vaultId, String usernameReader) {
        Vault vault = vaultsDao.getVault(vaultId);
        if (vault.getUsernameOwner().equals(usernameReader)) {
            vaultsDao.deleteVault(vaultId);
        } else {
            throw new ValidationException("You are not the owner of this vault");
        }
    }
}