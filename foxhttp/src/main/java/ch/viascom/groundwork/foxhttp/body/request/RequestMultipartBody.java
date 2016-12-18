package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.body.FoxHttpRequestBodyContext;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.util.NamedInputStream;
import lombok.ToString;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * RequestMultipartBody for FoxHttp
 * <p>
 * Stores multiple data for a request body
 *
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class RequestMultipartBody extends FoxHttpRequestBody {

    private final String boundary;
    private final Charset charset;
    private final String lineFeed;

    private HashMap<String, String> forms = new HashMap<>();
    private HashMap<String, NamedInputStream> stream = new HashMap<>();

    private PrintWriter writer;

    public RequestMultipartBody(Charset charset) {
        this(charset, "\n");
    }

    public RequestMultipartBody(Charset charset, String lineFeed) {
        this.charset = charset;
        this.lineFeed = lineFeed;
        this.boundary = "===" + System.currentTimeMillis() + "===";
    }

    @Override
    public void setBody(FoxHttpRequestBodyContext context) throws FoxHttpRequestException {
        try {
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

            processFormFields();
            processStream();

            writer.flush();
            writer.append("--" + boundary + "--").append(lineFeed);
            writer.close();

            //Execute interceptor
            executeInterceptor(context);

            context.getUrlConnection().getOutputStream().write(outputStream.toByteArray());

        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
        }

    }

    @Override
    public boolean hasBody() {
        return (stream.size() > 0 || forms.size() > 0);
    }

    @Override
    public ContentType getOutputContentType() {
        return ContentType.create("multipart/form-data; boundary=" + boundary, charset);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        forms.put(name, value);
    }

    private void processFormFields() {
        for (String key : forms.keySet()) {
            writer.append("--" + boundary).append(lineFeed);
            writer.append("Content-Disposition: form-data; name=\"" + key + "\"").append(lineFeed);
            writer.append("Content-Type: text/plain; charset=" + charset).append(lineFeed);
            writer.append(lineFeed);
            writer.append(forms.get(key)).append(lineFeed);
            writer.flush();
        }
    }

    /**
     * Adds a file to the request
     *
     * @param fieldName  name attribute
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile) throws FileNotFoundException {
        stream.put(fieldName, new NamedInputStream(uploadFile.getName(), new FileInputStream(uploadFile)));
    }

    /**
     * Adds an inputstream to te request
     *
     * @param name
     * @param inputStreamName
     * @param inputStream
     */
    public void addInputStreamPart(String name, String inputStreamName, InputStream inputStream) {
        stream.put(name, new NamedInputStream(inputStreamName, inputStream));
    }

    private void processStream() throws IOException {
        for (String key : stream.keySet()) {
            writer.append("--" + boundary).append(lineFeed);
            writer.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + stream.get(key).getName() + "\"").append(lineFeed);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(stream.get(key).getName())).append(lineFeed);
            writer.append("Content-Transfer-Encoding: binary").append(lineFeed);
            writer.append(lineFeed);
            writer.flush();

            InputStream inputStream = stream.get(key).getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            writer.append(lineFeed);
            writer.flush();
        }
    }


}
