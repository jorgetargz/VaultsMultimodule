package org.jorgetargz.security;

import org.jorgetargz.utils.modelo.ContentCiphed;

public interface Encriptacion {

    ContentCiphed encriptar(String texto, String secret);

    String desencriptar(ContentCiphed contentCiphed, String secret);

}
