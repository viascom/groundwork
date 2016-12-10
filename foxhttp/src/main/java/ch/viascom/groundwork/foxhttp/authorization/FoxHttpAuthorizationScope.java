package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.Getter;

import java.net.URL;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpAuthorizationScope {

    public static final FoxHttpAuthorizationScope ANY = new FoxHttpAuthorizationScope(null, null);

    @Getter
    private final URL url;
    @Getter
    private final RequestType requestType;


    /**
     * Given an url and requestType, constructs a FoxHttpAuthorizationScope.
     *
     * @param url         The url to use for the FoxHttpAuthorizationScope.
     * @param requestType The requestType to use for the FoxHttpAuthorizationScope.
     */
    FoxHttpAuthorizationScope(final URL url, final RequestType requestType) {
        this.url = url;
        this.requestType = requestType;
    }

    /**
     * Creates a new instance of {@link FoxHttpAuthorizationScope}.
     *
     * @param url         The url to use for the FoxHttpAuthorizationScope.
     * @param requestType The requestType to use for the FoxHttpAuthorizationScope.
     * @return FoxHttpAuthorizationScope
     */
    public static FoxHttpAuthorizationScope create(final URL url, final RequestType requestType) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null");
        }
        if (requestType == null) {
            throw new IllegalArgumentException("requestType can not be null");
        }
        if (url.toString().length() == 0) {
            throw new IllegalArgumentException("url can not be empty");
        }
        return new FoxHttpAuthorizationScope(url, requestType);
    }

    /**
     * Converts a FoxHttpAuthorizationScope to a string.
     */
    @Override
    public String toString() {
        return this.getRequestType() + " " + this.getUrl();
    }

}
