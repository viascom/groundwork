package ch.viascom.groundwork.foxhttp.body.response;

import ch.viascom.groundwork.foxhttp.body.FoxHttpBody;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class FoxHttpResponseBody implements FoxHttpBody {

    private ByteArrayOutputStream body = new ByteArrayOutputStream();

    public void setBody(ByteArrayOutputStream body) {
        this.body = body;
    }

    public void setBody(InputStream inputStream) throws IOException {
        setBody(inputStream, false);
    }

    public void setBody(InputStream inputStream, boolean overwrite) throws IOException {
        if (overwrite) {
            body = new ByteArrayOutputStream();
        }
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
