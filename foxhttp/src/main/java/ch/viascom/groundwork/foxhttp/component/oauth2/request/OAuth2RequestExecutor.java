package ch.viascom.groundwork.foxhttp.component.oauth2.request;

import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Component;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface OAuth2RequestExecutor {
    void executeOAuth2Request(FoxHttpRequest foxHttpRequest, OAuth2Component oAuth2Component) throws FoxHttpException;
}
