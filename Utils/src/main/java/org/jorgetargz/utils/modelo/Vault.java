package org.jorgetargz.utils.modelo;

import lombok.Data;

@Data
public class Vault {
    private int id;
    private String name;
    private String username;
    private String password;
    private boolean readByAll;
    private boolean writeByAll;
}
