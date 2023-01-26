package org.jorgetargz.client.domain.services.impl;

import com.google.gson.Gson;
import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Either;
import org.jorgetargz.client.dao.MessagesDAO;
import org.jorgetargz.client.dao.UsersDAO;
import org.jorgetargz.client.dao.VaultDAO;
import org.jorgetargz.client.dao.di.ProducerGson;
import org.jorgetargz.client.dao.impl.MessagesDAOImpl;
import org.jorgetargz.client.dao.impl.UsersDAOImpl;
import org.jorgetargz.client.dao.impl.VaultsDAOImpl;
import org.jorgetargz.client.dao.vault_api.MessagesAPI;
import org.jorgetargz.client.dao.vault_api.UsersAPI;
import org.jorgetargz.client.dao.vault_api.VaultAPI;
import org.jorgetargz.client.dao.vault_api.config.ConfigVaultAPI;
import org.jorgetargz.client.dao.vault_api.di.ProducerVaultAPI;
import org.jorgetargz.client.dao.vault_api.utils.AuthorizationInterceptor;
import org.jorgetargz.client.dao.vault_api.utils.CacheAuthorization;
import org.jorgetargz.client.domain.services.MessagesServices;
import org.jorgetargz.client.domain.services.UsersServices;
import org.jorgetargz.client.domain.services.VaultServices;
import org.jorgetargz.security.Encriptacion;
import org.jorgetargz.security.impl.EncriptacionAES;
import org.jorgetargz.utils.modelo.Message;
import org.jorgetargz.utils.modelo.User;
import org.jorgetargz.utils.modelo.Vault;
import retrofit2.Retrofit;

public class Test {

    public static void main(String[] args) {
        Encriptacion encriptacion = new EncriptacionAES();
        Gson gson = new ProducerGson().getGson();
        CacheAuthorization cacheAuthorization = new CacheAuthorization();
        cacheAuthorization.setUser("manolo");
        cacheAuthorization.setPassword("manolo");
        AuthorizationInterceptor authorizationInterceptor = new AuthorizationInterceptor(cacheAuthorization);
        ConfigVaultAPI config = new ConfigVaultAPI();
        Retrofit retrofit = new ProducerVaultAPI().getRetrofit(config, authorizationInterceptor, gson);
        MessagesAPI messagesAPI = new ProducerVaultAPI().getMessagesAPI(retrofit);
        MessagesDAO messagesDAO = new MessagesDAOImpl(new Gson(), messagesAPI);
        MessagesServices messagesServices = new MessagesServicesImpl(messagesDAO, encriptacion);

        Message message = Message.builder()
                .idVault(15)
                .contentUnsecured("Hola mundo")
                .build();
        //performCall(messagesServices.save(message, "ruben"));

        Message message2 = Message.builder()
                .id(18)
                .idVault(15)
                .contentUnsecured("Hola mundo 2")
                .build();
        //performCall(messagesServices.update(message2, "ruben"));

        int id = 7;
        //performCall(messagesServices.delete(id));

        //performCall(messagesServices.getAll("Caja rebuena", "ruben", "manolo"));


        UsersAPI usersAPI = new ProducerVaultAPI().getUsersAPI(retrofit);
        UsersDAO usersDAO = new UsersDAOImpl(new Gson(), usersAPI);
        UsersServices userServices = new UsersServicesImpl(usersDAO);

        User user = User.builder()
                .username("manolo")
                .password("manolo")
                .role("USER")
                .build();
        //performCall(userServices.save(user));
        //performCall(userServices.delete("manolo"));


        VaultAPI vaultAPI = new ProducerVaultAPI().getVaultAPI(retrofit);
        VaultDAO vaultDAO = new VaultsDAOImpl(new Gson(), vaultAPI);
        VaultServices vaultServices = new VaultServicesImpl(vaultDAO);

        //performCall(vaultServices.getAll());

        Vault vault = Vault.builder()
                .name("Caja la buena")
                .usernameOwner("manolo")
                .password("manolo")
                .writeByAll(true)
                .readByAll(true)
                .build();
        //performCall(vaultServices.save(vault));

        //performCall(vaultServices.delete(7));

        Vault credentials = Vault.builder()
                .id(15)
                .usernameOwner("ruben")
                .password("ruben")
                .build();
        //performCall(vaultServices.changePassword(credentials, "manolo"));
    }

    private static <T> void performCall(Single<Either<String, T>> call) {
        call.blockingSubscribe(
                either -> {
                    if (either.isRight()) {
                        System.out.println(either.get());
                    } else {
                        System.out.println(either.getLeft());
                    }
                });
    }
}