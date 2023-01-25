package org.jorgetargz.client.dao.vault_api;

import io.reactivex.rxjava3.core.Single;
import org.jorgetargz.utils.common.ConstantesAPI;
import org.jorgetargz.utils.modelo.Message;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface MessagesAPI {

    @GET(ConstantesAPI.ENDPOINT_GET_MESSAGES)
    Single<List<Message>> getMessages(@Path(ConstantesAPI.VAULT_ID_PARAM) int vaultId,
                                      @Query("vaultName") String vaultName,
                                      @Query("usernameOwner") String usernameOwner,
                                      @Query("password") String password);

    @POST(ConstantesAPI.ENDPOINT_MESSAGE)
    Single<Message> create(Message message, @Query("password") String password);

    @PUT(ConstantesAPI.ENDPOINT_MESSAGE)
    Single<Message> update(Message message, @Query("password") String password);

    @DELETE(ConstantesAPI.ENDPOINT_MESSAGE_DELETE)
    Single<Response<Void>> delete(@Path(ConstantesAPI.MESSAGE_ID_PARAM) int messageId);

}
