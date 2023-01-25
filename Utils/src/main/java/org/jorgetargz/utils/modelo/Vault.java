package org.jorgetargz.utils.modelo;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vault {
    private int id;
    private String name;
    private String usernameOwner;
    private String password;
    private boolean readByAll;
    private boolean writeByAll;
}
