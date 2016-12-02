package ch.viascom.groundwork.restclient.http.request.simple.filter;

import ch.viascom.groundwork.restclient.http.request.Request;
import ch.viascom.groundwork.restclient.http.request.filter.request.RequestFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BearerTokenFilter implements RequestFilter {

    private String token = "";

    @Override
    public void filter(Request request) throws Exception {
        request.addHeaders("Authorization", "Bearer " + token);
    }
}
