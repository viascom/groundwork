package ch.viascom.groundwork.foxhttp.body;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLConnection;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoxHttpRequestBodyContext {
    private URLConnection urlConnection;
    private FoxHttpRequest request;
    private FoxHttpClient client;
}
