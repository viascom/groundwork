package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.body.response.FoxHttpResponseBody;
import ch.viascom.groundwork.foxhttp.body.response.FoxHttpResponseInformation;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorExecutor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.*;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@NoArgsConstructor
public class FoxHttpResponse<T extends Serializable> {
    @Setter(AccessLevel.PROTECTED)
    private FoxHttpResponseBody responseBody = new FoxHttpResponseBody();

    @Setter(AccessLevel.PROTECTED)
    private FoxHttpResponseInformation responseInformation;

    @Setter(AccessLevel.PROTECTED)
    private int responseCode = -1;

    private FoxHttpHeader responseHeaders;

    @Setter(AccessLevel.PROTECTED)
    private FoxHttpClient foxHttpClient;

    @Setter(AccessLevel.PROTECTED)
    private FoxHttpRequest foxHttpRequest;

    public FoxHttpResponse(InputStream body, FoxHttpRequest foxHttpRequest, int responseCode, FoxHttpClient foxHttpClient, FoxHttpResponseInformation foxHttpResponseInformation) throws IOException, FoxHttpException {
        this.responseBody.setBody(body);
        this.foxHttpClient = foxHttpClient;
        this.responseCode = responseCode;
        this.foxHttpRequest = foxHttpRequest;
        this.responseInformation = foxHttpResponseInformation;
        //Execute interceptor
        FoxHttpInterceptorExecutor.executeResponseBodyInterceptor(
                new FoxHttpResponseBodyInterceptorContext(responseCode, this, foxHttpRequest, foxHttpClient)
        );
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

    public String getStringBody() throws IOException {
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

    public T getParsedBody(Class<T> parseClass) throws FoxHttpResponseException {
        if (foxHttpClient.getFoxHttpResponseParser() == null) {
            throw new FoxHttpResponseException("getParsedBody needs a FoxHttpResponseParser to deserialize the body");
        }
        try {
            return (T) foxHttpClient.getFoxHttpResponseParser().jsonToObject(getStringBody(), (Class<Serializable>) parseClass);
        } catch (IOException e) {
            throw new FoxHttpResponseException(e);
        }
    }
}
