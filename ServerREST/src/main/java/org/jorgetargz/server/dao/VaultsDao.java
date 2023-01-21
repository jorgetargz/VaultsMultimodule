package org.jorgetargz.server.dao;

import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.Vault;
import org.jorgetargz.utils.modelo.VaultCredential;

import java.util.List;

public interface VaultsDao {
    List<Vault> getVaults(String username);

    Vault getVault(VaultCredential vaultCredential);

    List<Message> getMessages(VaultCredential vaultCredential);

    Vault createVault(Vault vault);

    Message createMessage(VaultCredential vaultCredential, String message);

    Message updateMessage(VaultCredential vaultCredential, String message, int messageId);

    Message deleteMessage(VaultCredential vaultCredential, int messageId);

    Vault changePassword(Vault vault, String newPassword);
}
