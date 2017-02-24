package ch.viascom.groundwork.foxhttp.component.oauth2.interceptor;

import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.component.oauth2.GrantType;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Component;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestConnectionInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestConnectionInterceptorContext;
import ch.viascom.groundwork.foxhttp.util.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class OAuth2RequestInterceptor implements FoxHttpRequestConnectionInterceptor {

    @Getter
    @Setter
    private OAuth2Component oAuth2Component;

    @Getter
    @Setter
    private int weight;


    @Override
    public void onIntercept(FoxHttpRequestConnectionInterceptorContext context) throws FoxHttpException {
        try {
            if (isScopePresent(context)) {
                context.getClient().getFoxHttpLogger().log("   -> OAuth2 is needed for this request");
                if (!isAccessTokenValid()) {
                    context.getClient().getFoxHttpLogger().log("   -> New OAuth2 token is needed");
                    if (oAuth2Component.getOAuth2Store().getRefreshToken() != null && !oAuth2Component.getOAuth2Store().getRefreshToken().isEmpty()) {
                        FoxHttpRequest request = oAuth2Component.generateRequestForGrantType(GrantType.REFRESH_TOKEN);
                        oAuth2Component.getOAuth2RequestExecutor().executeOAuth2Request(request, oAuth2Component);
                    } else {
                        FoxHttpRequest request = oAuth2Component.generateRequestForGrantType(oAuth2Component.getOAuth2Store().getGrantType());
                        oAuth2Component.getOAuth2RequestExecutor().executeOAuth2Request(request, oAuth2Component);
                    }
                }
            }
        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
        }
    }

    private boolean isScopePresent(FoxHttpRequestConnectionInterceptorContext context) {
        boolean isPresent = false;
        for (FoxHttpAuthorizationScope scope : oAuth2Component.getOAuth2Store().getAuthScopes()) {
            isPresent = RegexUtil.doesURLMatch(context.getRequest().getRequestType() + " " + context.getClient().getFoxHttpPlaceholderStrategy().processPlaceholders(context.getUrl().toString(), context.getClient()),
                    context.getClient().getFoxHttpPlaceholderStrategy().processPlaceholders(scope.toString(), context.getClient()));
            if (isPresent) {
                break;
            }
        }
        return isPresent;
    }

    private boolean isAccessTokenValid() {
        if (oAuth2Component.getOAuth2Store().getAccessToken().isEmpty()) {
            return false;
        } else if (oAuth2Component.getOAuth2Store().getAccessTokenExpirationTime() != null &&
                oAuth2Component.getOAuth2Store().getAccessTokenExpirationTime().isBeforeNow()) {
            return false;
        }
        return true;
    }
}
