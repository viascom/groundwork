package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.util.NamedInputStream;
import lombok.Getter;
import lombok.ToString;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

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

    @Getter
    private HashMap<String, String> forms = new HashMap<>();
    @Getter
    private HashMap<String, NamedInputStream> stream = new HashMap<>();

    private PrintWriter writer;

    /**
     * Create a new RequestMultipartBody
     * <i>lineFeed is set to \n</i>
     *
     * @param charset used charset for the body
     */
    public RequestMultipartBody(Charset charset) {
        this(charset, "\n");
    }

    /**
     * Create a new RequestMultipartBody
     *
     * @param charset  used charset for the body
     * @param lineFeed linefeed used for the body
     */
    public RequestMultipartBody(Charset charset, String lineFeed) {
        this.charset = charset;
        this.lineFeed = lineFeed;
        this.boundary = "===" + System.currentTimeMillis() + "===";
    }

    /**
     * Set the body of the request
     *
     * @param context context of the request
     * @throws FoxHttpRequestException can throw different exception based on input streams and interceptors
     */
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

            //Add Content-Length header if not exist
            if (context.getUrlConnection().getRequestProperty(HeaderTypes.CONTENT_LENGTH.toString()) == null) {
                context.getUrlConnection().setRequestProperty(HeaderTypes.CONTENT_LENGTH.toString(), Integer.toString(outputStream.size()));
            }

            context.getUrlConnection().getOutputStream().write(outputStream.toByteArray());

        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
        }

    }

    /**
     * Checks if the body contains data
     *
     * @return true if data is stored in the body
     */
    @Override
    public boolean hasBody() {
        return stream.size() > 0 || forms.size() > 0;
    }

    /**
     * Get the ContentType of this body
     *
     * @return ContentType of this body
     */
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
        for (Map.Entry<String, String> entry : forms.entrySet()) {
            writer.append("--" + boundary).append(lineFeed);
            writer.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"").append(lineFeed);
            writer.append("Content-Type: text/plain; charset=" + charset.displayName()).append(lineFeed);
            writer.append(lineFeed);
            writer.append(entry.getValue()).append(lineFeed);
            writer.flush();
        }
    }

    /**
     * Adds a file to the request
     *
     * @param fieldName  name attribute
     * @param uploadFile a File to be uploaded
     * @throws FileNotFoundException
     */
    public void addFilePart(String fieldName, File uploadFile) throws FileNotFoundException {
        stream.put(fieldName, new NamedInputStream(uploadFile.getName(), new FileInputStream(uploadFile), "binary", URLConnection.guessContentTypeFromName(uploadFile.getName())));
    }

    /**
     * Adds an inputstream to te request
     *
     * @param name
     * @param inputStreamName
     * @param inputStream
     * @param contentTransferEncoding usually binary
     * @param contentType
     */
    public void addInputStreamPart(String name, String inputStreamName, InputStream inputStream, String contentTransferEncoding, String contentType) {
        stream.put(name, new NamedInputStream(inputStreamName, inputStream, contentTransferEncoding, contentType));
    }

    private void processStream() throws IOException {
        for (Map.Entry<String, NamedInputStream> entry : stream.entrySet()) {
            writer.append("--" + boundary).append(lineFeed);
            writer.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + entry.getValue().getName() + "\"").append(lineFeed);
            writer.append("Content-Type: " + entry.getValue().getType()).append(lineFeed);
            writer.append("Content-Transfer-Encoding: " + entry.getValue().getContentTransferEncoding()).append(lineFeed);
            writer.append(lineFeed);
            writer.flush();

            InputStream inputStream = entry.getValue().getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
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
