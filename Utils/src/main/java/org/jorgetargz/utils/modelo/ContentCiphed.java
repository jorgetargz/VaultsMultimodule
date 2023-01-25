package org.jorgetargz.utils.modelo;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentCiphed {
    private byte[] iv;
    private byte[] salt;
    private byte[] cipherText;
}
