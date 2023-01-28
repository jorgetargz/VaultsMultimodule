package org.jorgetargz.server.jakarta.security;

import org.jorgetargz.server.jakarta.common.Constantes;
import lombok.extern.log4j.Log4j2;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

@Log4j2
public class RsaJsonWebKeyProducer {

    private static RsaJsonWebKeyProducer instance;
    private RsaJsonWebKey rsaJsonWebKey;

    private RsaJsonWebKeyProducer() {
        try {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        } catch (JoseException e) {
            log.error(e.getMessage(), e);
        }
        rsaJsonWebKey.setKeyId(Constantes.KEY_ID);
    }

    public static RsaJsonWebKeyProducer getInstance() {
        if (instance == null) {
            instance = new RsaJsonWebKeyProducer();
        }
        return instance;
    }

    public RsaJsonWebKey getRSAKey() {
        return rsaJsonWebKey;
    }
}