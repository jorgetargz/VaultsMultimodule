package org.jorgetargz.server.domain.services.impl;

import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.extern.log4j.Log4j2;
import org.jorgetargz.security.Encriptacion;
import org.jorgetargz.server.dao.MessagesDao;
import org.jorgetargz.server.dao.VaultsDao;
import org.jorgetargz.server.domain.common.Constantes;
import org.jorgetargz.server.domain.services.ServicesVaults;
import org.jorgetargz.server.domain.services.excepciones.ValidationException;
import org.jorgetargz.utils.modelo.ContentCiphed;
import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.Vault;

import java.util.Base64;
import java.util.List;

@Log4j2
public class ServicesVaultsImpl implements ServicesVaults {

    private final VaultsDao vaultsDao;
    private final MessagesDao messageDao;
    private final Pbkdf2PasswordHash passwordHash;
    private final Encriptacion encriptacion;
    private final Base64.Decoder decoder;

    @Inject
    public ServicesVaultsImpl(VaultsDao vaultsDao, MessagesDao messageDao, Pbkdf2PasswordHash passwordHash, Encriptacion encriptacion) {
        this.vaultsDao = vaultsDao;
        this.messageDao = messageDao;
        this.passwordHash = passwordHash;
        this.encriptacion = encriptacion;
        this.decoder = Base64.getDecoder();
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
    public Vault getVault(Vault credentials, String usernameReader) {
        String password = new String(decoder.decode(credentials.getPassword()));
        String username = new String(decoder.decode(credentials.getUsernameOwner()));
        String name = new String(decoder.decode(credentials.getName()));
        Vault vault = vaultsDao.getVault(username, name);
        if (passwordHash.verify(password.toCharArray(), vault.getPassword())) {
            if (vault.getUsernameOwner().equals(usernameReader) || vault.isReadByAll()) {
                return vault;
            } else {
                throw new ValidationException(Constantes.ONLY_THE_OWNER_CAN_READ_THIS_VAULT);
            }
        } else {
            throw new ValidationException(Constantes.WRONG_CREDENTIALS);
        }
    }

    @Override
    public void changePassword(Vault credentials, String password, String usernameReader) {
        String newPassword = new String(Base64.getDecoder().decode(password));
        int vaultId = credentials.getId();
        Vault vault = vaultsDao.getVault(vaultId);
        if (passwordHash.verify(credentials.getPassword().toCharArray(), vault.getPassword())
                && vault.getUsernameOwner().equals(usernameReader)) {

            List<Message> messages = messageDao.getMessages(vaultId);
            for (Message message : messages) {
                String messageText = encriptacion.desencriptar(message.getContentCiphed(), credentials.getPassword());
                ContentCiphed contentCiphed = encriptacion.encriptar(messageText, newPassword);
                message.setContentCiphed(contentCiphed);
                messageDao.updateMessage(message);
            }

            String newPasswordHashed = passwordHash.generate(newPassword.toCharArray());
            vaultsDao.changePassword(vaultId, newPasswordHashed);
        } else {
            throw new ValidationException(Constantes.NOT_THE_OWNER_OF_THIS_VAULT_OR_THE_PASSWORD_IS_INCORRECT);
        }
    }

    @Override
    public void deleteVault(int vaultId, String usernameReader) {
        Vault vault = vaultsDao.getVault(vaultId);
        if (vault.getUsernameOwner().equals(usernameReader)) {
            vaultsDao.deleteVault(vaultId);
        } else {
            throw new ValidationException(Constantes.ONLY_THE_OWNER_OF_THE_VAULT_CAN_DELETE_IT);
        }
    }
}