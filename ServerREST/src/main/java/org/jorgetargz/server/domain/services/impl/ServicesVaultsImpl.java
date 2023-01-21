package org.jorgetargz.server.domain.services.impl;

import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.jorgetargz.security.Encriptacion;
import org.jorgetargz.server.dao.LoginDao;
import org.jorgetargz.server.dao.VaultsDao;
import org.jorgetargz.server.domain.services.ServicesVaults;
import org.jorgetargz.server.domain.services.excepciones.ValidationException;
import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.Vault;
import org.jorgetargz.utils.modelo.VaultCredential;

import java.util.List;

public class ServicesVaultsImpl implements ServicesVaults {

    private final VaultsDao vaultsDao;
    private final LoginDao loginDao;
    private final Pbkdf2PasswordHash passwordHash;
    private final Encriptacion encriptacion;

    @Inject
    public ServicesVaultsImpl(VaultsDao vaultsDao, LoginDao loginDao, Pbkdf2PasswordHash passwordHash, Encriptacion encriptacion) {
        this.vaultsDao = vaultsDao;
        this.loginDao = loginDao;
        this.passwordHash = passwordHash;
        this.encriptacion = encriptacion;
    }

    @Override
    public List<Message> getMessages(VaultCredential vaultCredential) {
        Vault vault = vaultsDao.getVault(vaultCredential);
        if (passwordHash.verify(vaultCredential.getPassword().toCharArray(), vault.getPassword())) {
            if (vaultCredential.getUsernameOwner().equals(vaultCredential.getUsernameReader()) || vault.isReadByAll()) {
                return vaultsDao.getMessages(vaultCredential);
            } else {
                throw new ValidationException("You don't have permission to read this vault");
            }
        } else {
            throw new ValidationException("Wrong password");
        }
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
    public Message createMessage(VaultCredential vaultCredential, String message) {
        Vault vault = vaultsDao.getVault(vaultCredential);
        if (passwordHash.verify(vaultCredential.getPassword().toCharArray(), vault.getPassword())) {
            if (vaultCredential.getUsernameOwner().equals(vaultCredential.getUsernameReader()) || vault.isWriteByAll()) {
                return vaultsDao.createMessage(vaultCredential, message);
            } else {
                throw new ValidationException("You don't have permission to write in this vault");
            }
        } else {
            throw new ValidationException("Wrong password");
        }
    }

    @Override
    public Message updateMessage(VaultCredential vaultCredential, String message, int messageId) {
        Vault vault = vaultsDao.getVault(vaultCredential);
        if (passwordHash.verify(vaultCredential.getPassword().toCharArray(), vault.getPassword())) {
            if (vaultCredential.getUsernameOwner().equals(vaultCredential.getUsernameReader()) || vault.isWriteByAll()) {
                return vaultsDao.updateMessage(vaultCredential, message, messageId);
            } else {
                throw new ValidationException("You don't have permission to write in this vault");
            }
        } else {
            throw new ValidationException("Wrong password");
        }
    }

    @Override
    public Message deleteMessage(VaultCredential vaultCredential, int messageId) {
        Vault vault = vaultsDao.getVault(vaultCredential);
        if (passwordHash.verify(vaultCredential.getPassword().toCharArray(), vault.getPassword())) {
            if (vaultCredential.getUsernameOwner().equals(vaultCredential.getUsernameReader()) || vault.isWriteByAll()) {
                return vaultsDao.deleteMessage(vaultCredential, messageId);
            } else {
                throw new ValidationException("You don't have permission to write in this vault");
            }
        } else {
            throw new ValidationException("Wrong password");
        }
    }

    @Override
    public Vault changePassword(VaultCredential vaultCredential, String newPassword) {
        Vault vault = vaultsDao.getVault(vaultCredential);
        if (passwordHash.verify(vaultCredential.getPassword().toCharArray(), vault.getPassword())) {
            List<Message> messages = getMessages(vaultCredential);
            for (Message message : messages) {
                deleteMessage(vaultCredential, message.getId());
                String messageText = encriptacion.desencriptar(message.getMessage(), vaultCredential.getPassword());
                createMessage(vaultCredential, encriptacion.encriptar(messageText, newPassword));
            }
            newPassword = passwordHash.generate(newPassword.toCharArray());
            return vaultsDao.changePassword(vault, newPassword);
        } else {
            throw new ValidationException("Wrong password");
        }
    }
}