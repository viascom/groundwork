package ch.viascom.groundwork.foxauth.util;

import ch.viascom.groundwork.foxauth.FoxAuthValidation;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;

/**
 * @author patrick.boesch@viascom.ch
 */
public class OAuth2FlowUtil {
    public static Response getAuthLoginRedirectResponse(FoxAuthValidation foxAuthValidation, String loginPath) throws URISyntaxException {
        UriBuilder uriBuilder = UriBuilder.fromPath(loginPath).queryParam("client_id", foxAuthValidation.getClientId());

        if (foxAuthValidation.getState() != null) {
            uriBuilder.queryParam("state", foxAuthValidation.getState());
        }
        if (foxAuthValidation.getRedirectURI() != null) {
            uriBuilder.queryParam("redirect_uri", foxAuthValidation.getRedirectURI());
        }

        return Response.temporaryRedirect(uriBuilder.build()).build();
    }

    public static Response getCodeRedirectResponse(String redirectURI, String state, String authorizationCode) throws URISyntaxException {
        UriBuilder uriBuilder;
        if (redirectURI != null) {
            uriBuilder = UriBuilder.fromPath(redirectURI);
        } else {
            uriBuilder = UriBuilder.fromPath("");
        }
        uriBuilder.queryParam("code", authorizationCode);

        if (state != null) {
            uriBuilder.queryParam("state", state);
        }

        return Response.status(Response.Status.FOUND).header("Location", uriBuilder.build()).build();
    }

    public static Response getTokenRedirectResponse(FoxAuthValidation foxAuthValidation, String accessToken, String tokenType, Long expiresIn) throws URISyntaxException {
        UriBuilder uriBuilder;
        if (foxAuthValidation.getRedirectURI() != null) {
            uriBuilder = UriBuilder.fromPath(foxAuthValidation.getRedirectURI());
        } else {
            uriBuilder = UriBuilder.fromPath("");
        }

        uriBuilder.queryParam("access_token", accessToken);
        uriBuilder.queryParam("token_type", tokenType);
        uriBuilder.queryParam("expires_in", expiresIn);
        uriBuilder.queryParam("scope", foxAuthValidation.getScopes());

        if (foxAuthValidation.getState() != null) {
            uriBuilder.queryParam("state", foxAuthValidation.getState());
        }

        return Response.status(Response.Status.FOUND).header("Location", uriBuilder.build()).build();
    }
}
