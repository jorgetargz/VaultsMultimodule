package org.jorgetargz.client.dao.vault_api;

import org.jorgetargz.utils.common.ConstantesAPI;
import org.jorgetargz.client.dao.common.Constantes;
import io.reactivex.rxjava3.core.Single;
import org.jorgetargz.utils.modelo.Login;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginAPI {

    @GET(ConstantesAPI.ENDPOINT_LOGIN)
    Single<Login> getReaderByLogin(@Header(Constantes.AUTHORIZATION) String authorization);

    @GET(ConstantesAPI.ENDPOINT_LOGOUT)
    Single<Response<Void>> logout(@Header(Constantes.AUTHORIZATION) String authorization);

    @POST(ConstantesAPI.ENDPOINT_LOGIN)
    Single<Login> registerReader(@Body Login login);
}
