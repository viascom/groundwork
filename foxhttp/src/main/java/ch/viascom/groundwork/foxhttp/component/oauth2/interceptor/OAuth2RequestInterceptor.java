package ch.viascom.groundwork.foxhttp.component.oauth2.interceptor;

import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
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
            if (RegexUtil.doesURLMatch(context.getRequest().getRequestType() + " " + context.getUrl().toString(),
                    oAuth2Component.getOAuth2Store().getAuthScope().toString())) {
                context.getClient().getFoxHttpLogger().log("   -> OAuth2 is needed for this request");
                if (!isAccessTokenValid()) {
                    context.getClient().getFoxHttpLogger().log("   -> New OAuth2 token is needed");
                    FoxHttpRequest request = oAuth2Component.generateRequestForGrantType(oAuth2Component.getOAuth2Store().getGrantType());
                    oAuth2Component.getOAuth2RequestExecutor().executeOAuth2Request(request, oAuth2Component);
                }
            }
        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
        }
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
