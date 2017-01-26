package ch.viascom.groundwork.foxauth.validator.type;

import lombok.Getter;

/**
 * @author patrick.boesch@viascom.ch
 */
public class AuthValidatorType {

    //OAuth2
    public static final AuthValidatorType OAUTH2_AUTHORIZATION_CODE_FLOW = create("oauth2_authorization_code_flow");
    public static final AuthValidatorType OAUTH2_IMPLICIT_FLOW = create("oauth2_implicit_flow");
    public static final AuthValidatorType OAUTH2_AUTHORIZATION_CODE = create("oauth2_authorization_code");
    public static final AuthValidatorType OAUTH2_PASSWORD = create("oauth2_password");
    public static final AuthValidatorType OAUTH2_CLIENT_CREDENTIALS = create("oauth2_client_credentials");
    public static final AuthValidatorType OAUTH2_REFRESH_TOKEN = create("oauth2_refresh_token");

    //Basic
    public static final AuthValidatorType BASIC_AUTHENTICATION = create("basic_authentication");
    public static final AuthValidatorType BAERER_AUTHENTICATION = create("baerer_authentication");

    @Getter
    private final String type;

    AuthValidatorType(final String type) {
        this.type = type;
    }

    public static AuthValidatorType create(final String authValidatorType) {
        if (authValidatorType == null) {
            throw new IllegalArgumentException("AuthValidatorType may not be null");
        }
        String type = authValidatorType.trim().toLowerCase();
        if (type.length() == 0) {
            throw new IllegalArgumentException("AuthValidatorType may not be empty");
        }
        return new AuthValidatorType(authValidatorType);
    }


}
