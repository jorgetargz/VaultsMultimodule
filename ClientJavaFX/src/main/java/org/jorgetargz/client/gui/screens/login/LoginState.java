package org.jorgetargz.client.gui.screens.login;

import org.jorgetargz.utils.modelo.Login;

public record LoginState(Login login, String error, boolean readerRegistered, boolean isLoading,
                         boolean isLoaded) {
}
