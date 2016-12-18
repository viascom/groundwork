package ch.viascom.groundwork.foxhttp.response;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.body.response.FoxHttpResponseBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.serviceresult.ServiceResult;
import ch.viascom.groundwork.serviceresult.ServiceResultStatus;
import ch.viascom.groundwork.serviceresult.exception.ServiceFault;
import ch.viascom.groundwork.serviceresult.util.Metadata;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class FoxHttpServiceResultParser<T extends Serializable> {

    private ServiceResultStatus status;
    private Class<T> type;
    @Getter(AccessLevel.PRIVATE)
    private T content;
    private String hash;
    private String destination;
    private HashMap<String, Metadata> metadata = new HashMap<>();

    private FoxHttpResponseBody responseBody = new FoxHttpResponseBody();
    private int responseCode = -1;
    private FoxHttpHeader responseHeaders;
    private FoxHttpClient foxHttpClient;
    private FoxHttpRequest foxHttpRequest;

    private Gson parser = new Gson();


    public FoxHttpServiceResultParser(FoxHttpResponse foxHttpResponse) throws IOException, FoxHttpException {
        this.responseBody = foxHttpResponse.getResponseBody();
        this.foxHttpClient = foxHttpResponse.getFoxHttpClient();
        this.responseCode = foxHttpResponse.getResponseCode();
        this.foxHttpRequest = foxHttpResponse.getFoxHttpRequest();
        this.responseHeaders = foxHttpResponse.getResponseHeaders();

    }

    public InputStream getInputStreamBody() {
        return new ByteArrayInputStream(responseBody.getBody().toByteArray());
    }

    public ByteArrayOutputStream getByteArrayOutputStreamBody() {
        return responseBody.getBody();
    }

    protected void setBody(InputStream body) throws IOException {
        this.responseBody.setBody(body);
    }

    private String getStringBody() throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(getInputStreamBody()));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\n');
        }
        rd.close();
        return response.toString();
    }

    public T getContent(Class<T> contentClass) {
        try {

            Type type = new ServiceResultParameterizedType(contentClass);

            ServiceResult<T> result = parser.fromJson(getStringBody(), type);

            this.type = result.getType();
            this.hash = result.getHash();
            this.destination = result.getDestination();
            this.metadata = result.getMetadata();

            return result.getContent();
        } catch (IOException e) {
            return null;
        }

    }

    public ServiceFault getFault() {
        try {
            ServiceResult<ServiceFault> result = parser.fromJson(getStringBody(), new TypeToken<ServiceResult<ServiceFault>>() {
            }.getType());

            this.hash = result.getHash();
            this.destination = result.getDestination();
            this.metadata = result.getMetadata();

            return result.getContent();
        } catch (IOException e) {
            return null;
        }

    }
}
