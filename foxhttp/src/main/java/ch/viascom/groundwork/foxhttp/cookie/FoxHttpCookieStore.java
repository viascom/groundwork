package ch.viascom.groundwork.foxhttp.cookie;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpCookieStore {
    Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException;

    void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException;
}
