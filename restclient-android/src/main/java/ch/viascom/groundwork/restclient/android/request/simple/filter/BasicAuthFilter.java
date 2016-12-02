package ch.viascom.groundwork.restclient.android.request.simple.filter;


import ch.viascom.groundwork.restclient.android.request.Request;
import ch.viascom.groundwork.restclient.android.request.filter.request.RequestFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okhttp3.Credentials;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthFilter implements RequestFilter {

    private String user = "";
    private String password = "";

    @Override
    public void filter(Request request) throws Exception {
        request.addHeaders("Authorization", Credentials.basic(user, password));
    }
}
