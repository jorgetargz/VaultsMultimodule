package org.jorgetargz.utils.modelo;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private int id;
    private int idVault;
    private ContentCiphed contentCiphed;
    private String contentUnsecured;

    @Override
    public String toString() {
        return "" + id;
    }
}
