package org.jorgetargz.client.dao.vault_api;

import io.reactivex.rxjava3.core.Single;
import org.jorgetargz.utils.common.ConstantesAPI;
import org.jorgetargz.utils.modelo.Vault;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface VaultAPI {

    @GET(ConstantesAPI.ENDPOINT_VAULT)
    Single<List<Vault>> getVaults();

    @POST(ConstantesAPI.ENDPOINT_VAULT)
    Single<Vault> createVault(@Body Vault vault);

    @PATCH(ConstantesAPI.ENDPOINT_VAULT_CHANGE_PASSWORD)
    Single<Response<Void>> changePassword(@Body Vault credentials, @Query("password") String password);

    @DELETE(ConstantesAPI.ENDPOINT_VAULT_DELETE)
    Single<Response<Void>> deleteVault(@Path(ConstantesAPI.VAULT_ID_PARAM) int vaultId);
}
