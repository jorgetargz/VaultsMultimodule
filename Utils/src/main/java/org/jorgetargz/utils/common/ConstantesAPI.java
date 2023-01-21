package org.jorgetargz.utils.common;

public class ConstantesAPI {

    public static final String API_PATH = "api/";

    //Main Paths for REST /api
    public static final String PATH_LOGIN = "login/";
    public static final String PATH_VAULT = "vault/";

    //Auxiliar Paths for REST /api/mainPath/auxPath
    public static final String LOGOUT_PATH = "logout/";
    public static final String VAULT_CHANGE_PASSWORD_PATH = "changePassword/";
    public static final String VAULT_MESSAGES_PATH = "messages/";
    public static final String MESSAGE_CREATE_PATH = "createMessage/";
    public static final String MESSAGE_UPDATE_PATH = "updateMessage/";
    public static final String MESSAGE_DELETE_PATH = "deleteMessage/";

    //ENDPOINTS LOGIN
    public static final String ENDPOINT_LOGIN = PATH_LOGIN;
    public static final String ENDPOINT_LOGOUT = PATH_LOGIN + LOGOUT_PATH;

    //ENDPOINTS VAULT
    public static final String ENDPOINT_VAULT = PATH_VAULT;
    public static final String ENDPOINT_VAULT_CHANGE_PASSWORD = PATH_VAULT + VAULT_CHANGE_PASSWORD_PATH;
    public static final String ENDPOINT_VAULT_MESSAGES = PATH_VAULT + VAULT_MESSAGES_PATH;
    public static final String ENDPOINT_MESSAGE_CREATE = PATH_VAULT + MESSAGE_CREATE_PATH;
    public static final String ENDPOINT_MESSAGE_UPDATE = PATH_VAULT + MESSAGE_UPDATE_PATH;
    public static final String ENDPOINT_MESSAGE_DELETE = PATH_VAULT + MESSAGE_DELETE_PATH;

    //Parameters
    public static final String ID = "id";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String MESSAGE_PARAM = "message";
    public static final String MESSAGE_ID_PARAM = "messageId";
    public static final String QUERY_PARAM_USERNAME = "username";
    public static final String QUERY_PARAM_PASSWORD = "password";

    private ConstantesAPI() {
    }
}
