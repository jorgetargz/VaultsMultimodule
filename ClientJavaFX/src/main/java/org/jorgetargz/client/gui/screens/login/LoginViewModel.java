package org.jorgetargz.client.gui.screens.login;

import org.jorgetargz.client.gui.screens.common.ScreenConstants;
import org.jorgetargz.client.domain.services.LoginServices;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jorgetargz.utils.modelo.User;

public class LoginViewModel {

    private final LoginServices loginServices;
    private final ObjectProperty<LoginState> state;

    @Inject
    public LoginViewModel(LoginServices loginServices) {
        this.loginServices = loginServices;
        state = new SimpleObjectProperty<>(new LoginState(null, null, false, false, false));
    }

    public ReadOnlyObjectProperty<LoginState> getState() {
        return state;
    }

    public void doLogin(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            state.set(new LoginState(null, ScreenConstants.FILL_ALL_THE_INPUTS, false, false, true));
            return;
        }
        state.set(new LoginState(null, null, false, true, false));
        loginServices.scGet(username, password)
                .observeOn(Schedulers.single())
                .subscribe(either -> {
                    if (either.isLeft())
                        state.set(new LoginState(null, either.getLeft(), false, false, true));
                    else {
                        state.set(new LoginState(either.get(), null, false, false, true));
                    }
                });
    }

    public void doRegister(String inputUsername, String inputPassword) {
        if (inputUsername != null && !inputUsername.isEmpty()
                && inputPassword != null && !inputPassword.isEmpty()) {
            User user = new User(inputUsername, inputPassword, "USER");
            state.set(new LoginState(null, null, false, true, false));
            loginServices.scRegisterUser(user)
                    .subscribeOn(Schedulers.single())
                    .subscribe(either -> {
                        if (either.isLeft())
                            state.set(new LoginState(null, either.getLeft(), false, false, true));
                        else {
                            state.set(new LoginState(null, null, true, false, true));
                        }
                    });
        } else {
            state.set(new LoginState(null, ScreenConstants.FILL_ALL_THE_INPUTS, false, false, true));
        }
    }

    public void clenState() {
        state.setValue(new LoginState(null, null, false, false, false));
    }
}
