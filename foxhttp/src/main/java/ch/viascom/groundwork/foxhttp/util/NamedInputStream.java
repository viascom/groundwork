package ch.viascom.groundwork.foxhttp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

/**
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
