package org.jorgetargz.server.domain.services;

import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.Vault;
import org.jorgetargz.utils.modelo.VaultCredential;

import java.util.List;

public interface ServicesVaults {

    List<Vault> getVaults(String username);

    Vault createVault(Vault vault);

    List<Message> getMessages(VaultCredential vaultCredential);

    Message createMessage(VaultCredential vaultCredential, String message);

    Message updateMessage(VaultCredential vaultCredential, String message, int messageId);

    Message deleteMessage(VaultCredential vaultCredential, int messageId);

    Vault changePassword(VaultCredential vault, String newPassword);
}
