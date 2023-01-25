package org.jorgetargz.utils.modelo;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private String role;
}