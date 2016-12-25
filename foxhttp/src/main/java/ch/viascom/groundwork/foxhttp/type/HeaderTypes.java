package ch.viascom.groundwork.foxhttp.type;

import lombok.Getter;

import java.util.Locale;

/**
 * @author patrick.boesch@viascom.ch
 */
public class HeaderTypes {

    // constants
    public static final HeaderTypes CONTENT_TYPE = create("Content-Type");
    public static final HeaderTypes CONTENT_LENGTH = create("Content-Length");
    public static final HeaderTypes CONTENT_ENCODING = create("Content-Encoding");
    public static final HeaderTypes CONTENT_MD5 = create("Content-MD5");
    public static final HeaderTypes USER_AGENT = create("User-Agent");

    public static final HeaderTypes AUTHORIZATION = create("Authorization");

    public static final HeaderTypes PROXY_AUTHORIZATION = create("Proxy-Authorization");
    public static final HeaderTypes PROXY_AUTHENTICATION = create("Proxy-Authenticate");

    public static final HeaderTypes ETAG= create("ETag");
    public static final HeaderTypes EXPIRES= create("Expires");
    public static final HeaderTypes IF_MATCH= create("If-Match");
    public static final HeaderTypes IF_MODIFIED_SINCE= create("If-Modified-Since");
    public static final HeaderTypes IF_NONE_MATCH= create("If-None-Match");

    public static final HeaderTypes ACCEPT = create("Accept");
    public static final HeaderTypes ACCEPT_CHARSET = create("Accept-Charset");
    public static final HeaderTypes ACCEPT_ENCODING = create("Accept-Encoding");
    public static final HeaderTypes ACCEPT_LANGUAGE = create("Accept-Language");

    @Getter
    private final String name;

    /**
     * Given a name, constructs a HeaderTypes.
     *
     * @param name The name to use for the HeaderType.
     */
    HeaderTypes(final String name) {
        this.name = name;
    }

    /**
     * Creates a new instance of {@link HeaderTypes}.
     *
     * @param name The name to use for the HeaderType.
     * @return header types
     */
    public static HeaderTypes create(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("header name may not be null");
        }
        String type = name.trim().toLowerCase(Locale.ENGLISH);
        if (type.length() == 0) {
            throw new IllegalArgumentException("header name may not be empty");
        }
        return new HeaderTypes(name);
    }


    /**
     * Converts a HeaderType to a string which can be used as a Header name.
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
