package ch.viascom.groundwork.foxhttp.component.oauth2.request;

import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Component;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Store;
import ch.viascom.groundwork.foxhttp.body.request.RequestUrlEncodedFormBody;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.query.FoxHttpRequestQuery;
import ch.viascom.groundwork.foxhttp.type.RequestType;

import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class OAuth2RequestGenerator {
    public static void setData(OAuth2Store oAuth2Store, FoxHttpRequestBuilder foxHttpRequestBuilder, HashMap<String, String> data) {
        if (oAuth2Store.getAuthRequestType() == RequestType.POST) {
            RequestUrlEncodedFormBody requestUrlEncodedFormBody = new RequestUrlEncodedFormBody(data);
            foxHttpRequestBuilder.setRequestBody(requestUrlEncodedFormBody);
        } else {
            FoxHttpRequestQuery foxHttpRequestQuery = new FoxHttpRequestQuery(data);
            foxHttpRequestBuilder.setRequestQuery(foxHttpRequestQuery);
        }
    }

    public abstract FoxHttpRequest getRequest(OAuth2Component oAuth2Component) throws MalformedURLException, FoxHttpRequestException;
}
