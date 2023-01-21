package org.jorgetargz.client.dao.vault_api.di;

import com.google.gson.Gson;
import org.jorgetargz.client.dao.vault_api.LoginAPI;
import org.jorgetargz.client.dao.vault_api.utils.AuthorizationInterceptor;
import org.jorgetargz.client.dao.vault_api.config.ConfigVaultAPI;
import org.jorgetargz.client.dao.vault_api.utils.CacheAuthorization;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;


public class ProducerVaultAPI {

    @Produces
    @Singleton
    public Retrofit getRetrofit(ConfigVaultAPI configVaultAPI, CacheAuthorization cache, Gson gson) {

        OkHttpClient clientOK = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(1, 1, TimeUnit.SECONDS))
                .addInterceptor(new AuthorizationInterceptor(cache))
                .build();

        return new Retrofit.Builder()
                .baseUrl(configVaultAPI.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(clientOK)
                .build();

    }

    @Produces
    @Singleton
    public LoginAPI getLoginAPI(Retrofit retrofit) {
        return retrofit.create(LoginAPI.class);
    }

}
