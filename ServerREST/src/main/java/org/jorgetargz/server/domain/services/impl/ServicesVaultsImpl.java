package org.jorgetargz.server.domain.services.impl;

import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.extern.log4j.Log4j2;
import org.jorgetargz.security.Encriptacion;
import org.jorgetargz.server.dao.MessagesDao;
import org.jorgetargz.server.dao.VaultsDao;
import org.jorgetargz.server.dao.excepciones.NotFoundException;
import org.jorgetargz.server.domain.services.ServicesVaults;
import org.jorgetargz.server.domain.services.excepciones.ValidationException;
import org.jorgetargz.utils.modelo.*;

import java.util.Base64;
import java.util.List;

@Log4j2
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
        String newPssword = new String(Base64.getDecoder().decode(password));
        int vaultId = credentials.getId();
        Vault vault = vaultsDao.getVault(vaultId);
        if (passwordHash.verify(credentials.getPassword().toCharArray(), vault.getPassword())
                && vault.getUsernameOwner().equals(usernameReader)) {
            try {
                List<Message> messages = messageDao.getMessages(vaultId);
                for (Message message : messages) {
                    String messageText = encriptacion.desencriptar(message.getContentCiphed(), credentials.getPassword());
                    ContentCiphed contentCiphed = encriptacion.encriptar(messageText, newPssword);
                    message.setContentCiphed(contentCiphed);
                    messageDao.updateMessage(message);
                }
            } catch (NotFoundException e) {
                log.info("No messages found for vault while changing vault password");
            }
            newPssword = passwordHash.generate(newPssword.toCharArray());
            vaultsDao.changePassword(vaultId, newPssword);
        } else {
            throw new ValidationException("You are not the owner of this vault");
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