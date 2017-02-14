package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.Getter;

/**
 * FoxHttpAuthorizationScope
 * <p>
 * Stores information about the scope for an Authorization
 *
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpAuthorizationScope {

    public static final FoxHttpAuthorizationScope ANY = new FoxHttpAuthorizationScope("*", null);

    @Getter
    private final String url;
    @Getter
    private final String requestType;


    /**
     * Given an url and requestType, constructs a FoxHttpAuthorizationScope.
     *
     * @param url         The url to use for the FoxHttpAuthorizationScope.
     * @param requestType The requestType to use for the FoxHttpAuthorizationScope.
     */
    FoxHttpAuthorizationScope(final String url, final String requestType) {
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
    public static FoxHttpAuthorizationScope create(final String url, final RequestType requestType) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null");
        }
        if (requestType == null) {
            throw new IllegalArgumentException("requestType can not be null");
        }
        if (url.length() == 0) {
            throw new IllegalArgumentException("url can not be empty");
        }
        return new FoxHttpAuthorizationScope(url, requestType.toString());
    }

    /**
     * Creates a new instance of {@link FoxHttpAuthorizationScope} with requestType ANY.
     *
     * @param url         The url to use for the FoxHttpAuthorizationScope.
     * @return FoxHttpAuthorizationScope
     */
    public static FoxHttpAuthorizationScope create(final String url) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null");
        }
        if (url.length() == 0) {
            throw new IllegalArgumentException("url can not be empty");
        }
        return new FoxHttpAuthorizationScope(url, "*");
    }

    /**
     * Converts a FoxHttpAuthorizationScope to a string.
     */
    @Override
    public String toString() {
        return ((this.getRequestType() == null) ? "" : this.getRequestType() + " ") + this.getUrl();
    }

}
