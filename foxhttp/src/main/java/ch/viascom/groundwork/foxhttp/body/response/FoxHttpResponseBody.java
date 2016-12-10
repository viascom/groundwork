package ch.viascom.groundwork.foxhttp.body.response;

import ch.viascom.groundwork.foxhttp.body.FoxHttpBody;
import lombok.Data;

import java.io.*;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class FoxHttpResponseBody implements FoxHttpBody {

    private ByteArrayOutputStream body = new ByteArrayOutputStream();

    public FoxHttpResponseBody() {
    }

    public void setBody(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                body.write(buffer, 0, len);
            }
            body.flush();
        }
    }
}
