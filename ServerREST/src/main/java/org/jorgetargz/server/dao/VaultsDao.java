package org.jorgetargz.server.dao;

import org.jorgetargz.utils.modelo.Vault;

import java.util.List;

public interface VaultsDao {
    List<Vault> getVaults(String username);

    Vault getVault(int vaultId);

    Vault createVault(Vault vault);

    void changePassword(int vaultId, String newPassword);

    void deleteVault(int vaultId);
}
