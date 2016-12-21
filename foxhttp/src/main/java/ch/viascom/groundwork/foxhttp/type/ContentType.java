package ch.viascom.groundwork.foxhttp.type;

import lombok.Getter;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ContentType {

    private static final String UTF8 = "UTF-8";
    
    // constants
    public static final ContentType APPLICATION_ATOM_XML = create("application/atom+xml", Charset.forName(UTF8));
    public static final ContentType APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", Charset.forName(UTF8));
    public static final ContentType APPLICATION_JSON = create("application/json", Charset.forName(UTF8));
    public static final ContentType APPLICATION_OCTET_STREAM = create("application/octet-stream", (Charset) null);
    public static final ContentType APPLICATION_SVG_XML = create("application/svg+xml", Charset.forName(UTF8));
    public static final ContentType APPLICATION_XHTML_XML = create("application/xhtml+xml", Charset.forName(UTF8));
    public static final ContentType APPLICATION_XML = create("application/xml", Charset.forName(UTF8));
    public static final ContentType MULTIPART_FORM_DATA = create("multipart/form-data", Charset.forName(UTF8));
    public static final ContentType TEXT_HTML = create("text/html", Charset.forName(UTF8));
    public static final ContentType TEXT_PLAIN = create("text/plain", Charset.forName(UTF8));
    public static final ContentType TEXT_XML = create("text/xml", Charset.forName(UTF8));
    public static final ContentType WILDCARD = create("*/*", (Charset) null);

    // defaults
    public static final ContentType DEFAULT_TEXT = TEXT_PLAIN;
    public static final ContentType DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
    public static final ContentType DEFAULT_JSON = APPLICATION_JSON;

    @Getter
    private final String mimeType;
    @Getter
    private final Charset charset;

    /**
     * Given a MIME type and a character set, constructs a ContentType.
     *
     * @param mimeType The MIME type to use for the ContentType header.
     * @param charset  The optional character set to use with the ContentType header.
     * @throws java.nio.charset.UnsupportedCharsetException If no support for the named charset is available in this Java virtual machine
     */
    ContentType(final String mimeType, final Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
    }

    /**
     * Creates a new instance of {@link ContentType}.
     *
     * @param mimeType MIME type. It may not be <code>null</code> or empty. It may not contain
     *                 characters &lt;"&gt;, &lt;;&gt;, &lt;,&gt; reserved by the HTTP specification.
     * @param charset  charset.
     * @return content type
     */
    public static ContentType create(final String mimeType, final Charset charset) {
        if (mimeType == null) {
            throw new IllegalArgumentException("MIME type may not be null");
        }
        String type = mimeType.trim().toLowerCase(Locale.US);
        if (type.length() == 0) {
            throw new IllegalArgumentException("MIME type may not be empty");
        }
        if (!valid(type)) {
            throw new IllegalArgumentException("MIME type may not contain reserved characters");
        }
        return new ContentType(type, charset);
    }

    private static boolean valid(final String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '"' || ch == ',') {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts a ContentType to a string which can be used as a ContentType header.
     * If a charset is provided by the ContentType, it will be included in the string.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.mimeType);
        if (this.charset != null) {
            buf.append("; charset=");
            buf.append(this.charset.name());
        }
        return buf.toString();
    }

}
