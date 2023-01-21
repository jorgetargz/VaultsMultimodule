package org.jorgetargz.utils.modelo;

import lombok.Data;

@Data
public class VaultCredential {
    private String vaultName;
    private String usernameOwner;
    private String usernameReader;
    private String password;
}
