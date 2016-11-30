package ch.viascom.groundwork.restclient.http.request.simple.filter;

import ch.viascom.groundwork.restclient.http.request.Request;
import ch.viascom.groundwork.restclient.http.request.filter.request.RequestWriteFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthFilter implements RequestWriteFilter {

    private String user = "";
    private String password = "";

    @Override
    public void filter(HttpRequestBase httpRequest, Request request) throws Exception {
        HttpHost targetHost = new HttpHost(httpRequest.getURI().getHost(), httpRequest.getURI().getPort(), httpRequest.getURI().getScheme());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(getUser(), getPassword()));

        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());

        // Add AuthCache to the execution context
        final HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        request.setHttpClientContext(context);
    }
}
