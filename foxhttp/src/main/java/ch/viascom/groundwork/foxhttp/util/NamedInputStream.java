package ch.viascom.groundwork.foxhttp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

/**
 * The NamedInputStream is an extended InputStream which can be viewed as a wrapper class of this set-up.
 * Can be used in a multipart body request. {@link ch.viascom.groundwork.foxhttp.body.request.RequestMultipartBody RequestMultipartBody}
 *
 * @author patrick.boesch@viascom.ch
 */
@Getter
@AllArgsConstructor
public class NamedInputStream {
    private String name;
    private InputStream inputStream;
    private String contentTransferEncoding;
    private String type;
}
