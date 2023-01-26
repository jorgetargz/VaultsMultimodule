package org.jorgetargz.utils.common;

public class ConstantesAPI {

    public static final String API_PATH = "api/";

    //Main Paths for REST /api
    public static final String PATH_LOGIN = "login/";
    public static final String PATH_USERS = "users/";
    public static final String PATH_VAULTS = "vaults/";
    public static final String PATH_MESSAGES = "messages/";

    //Auxiliar Paths for REST /api/mainPath/auxPath
    public static final String LOGOUT_PATH = "logout/";
    public static final String VAULT_CHANGE_PASSWORD_PATH = "changePassword/";
    public static final String VAULT_PATH = "vault/";

    //Path Parameters
    public static final String VAULT_ID_PATH_PARAM = "{vaultId}";
    public static final String VAULT_ID_PARAM = "vaultId";
    public static final String MESSAGE_ID_PATH_PARAM = "{messageId}";
    public static final String MESSAGE_ID_PARAM = "messageId";
    public static final String USERNAME_PATH_PARAM = "{username}";
    public static final String USERNAME_PARAM = "username";

    //ENDPOINTS LOGIN
    public static final String ENDPOINT_LOGIN = PATH_LOGIN;
    public static final String ENDPOINT_LOGOUT = PATH_LOGIN + LOGOUT_PATH;

    //ENDPOINTS VAULT
    public static final String ENDPOINT_VAULT = PATH_VAULTS;
    public static final String ENDPOINT_VAULT_GET = PATH_VAULTS + VAULT_PATH;
    public static final String ENDPOINT_VAULT_CHANGE_PASSWORD = PATH_VAULTS + VAULT_CHANGE_PASSWORD_PATH;
    public static final String ENDPOINT_VAULT_DELETE = PATH_VAULTS + VAULT_ID_PATH_PARAM;

    //ENDPOINTS MESSAGES
    public static final String ENDPOINT_MESSAGES = PATH_MESSAGES;
    public static final String ENDPOINT_MESSAGE_DELETE = PATH_MESSAGES + MESSAGE_ID_PATH_PARAM;

    //ENDPOINTS USERS
    public static final String ENDPOINT_USERS = PATH_USERS;
    public static final String ENDPOINT_USER_DELETE = PATH_USERS + USERNAME_PATH_PARAM;

    //Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    private ConstantesAPI() {
    }
}
