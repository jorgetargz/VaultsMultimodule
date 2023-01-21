package org.jorgetargz.security;

import org.jorgetargz.security.impl.EncriptacionAES;

public class Test {
    public static void main(String[] args) {
        Encriptacion encriptacion = new EncriptacionAES();
        String encriptado = encriptacion.encriptar("Hola", "1234");
        System.out.println(encriptado);
        String desencriptado = encriptacion.desencriptar(encriptado, "1234");
        System.out.println(desencriptado);
    }
}
