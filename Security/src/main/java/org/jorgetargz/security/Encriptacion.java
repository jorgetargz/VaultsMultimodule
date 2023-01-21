package org.jorgetargz.security;

public interface Encriptacion {

    String encriptar(String texto, String secret);

    String desencriptar(String texto, String secret);

}
