package org.jorgetargz.utils.modelo;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Message {
    private int id;
    private int idVault;
    private ContentCiphed contentCiphed;
}
