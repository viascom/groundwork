package ch.viascom.groundwork.foxhttp.component.oauth2;

import lombok.Getter;

/**
 * @author patrick.boesch@viascom.ch
 */
public class GrantType {

    @Getter
    private final String type;

    public static final GrantType AUTHORIZATION_CODE = create("authorization_code");
    public static final GrantType PASSWORD = create("password");
    public static final GrantType CLIENT_CREDENTIALS = create("client_credentials");
    public static final GrantType REFRESH_TOKEN = create("refresh_token");


    GrantType(final String type) {
        this.type = type;
    }

    public static GrantType create(final String grantType) {
        if (grantType == null) {
            throw new IllegalArgumentException("GrantType may not be null");
        }
        String type = grantType.trim().toLowerCase();
        if (type.length() == 0) {
            throw new IllegalArgumentException("GrantType may not be empty");
        }
        return new GrantType(grantType);
    }

}
