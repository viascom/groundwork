package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.body.response.FoxHttpResponseBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorExecutor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@NoArgsConstructor
public class FoxHttpResponse<T extends Serializable> {

    private FoxHttpResponseBody responseBody = new FoxHttpResponseBody();

    private int responseCode = -1;

    private FoxHttpHeader responseHeaders;

    private FoxHttpClient foxHttpClient;

    private FoxHttpRequest foxHttpRequest;

    public FoxHttpResponse(InputStream body, FoxHttpRequest foxHttpRequest, int responseCode, FoxHttpClient foxHttpClient) throws IOException, FoxHttpException {
        this.responseBody.setBody(body);
        this.foxHttpClient = foxHttpClient;
        this.responseCode = responseCode;
        this.foxHttpRequest = foxHttpRequest;
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
            if (response.length() != 0) {
                response.append('\n');
            }
            response.append(line);
        }
        rd.close();
        return response.toString();
    }

    public T getParsedBody(Class<T> parseClass) throws FoxHttpResponseException {
        if (foxHttpClient.getFoxHttpResponseParser() == null) {
            throw new FoxHttpResponseException("getParsedBody needs a FoxHttpResponseParser to deserialize the body");
        }
        try {
            return (T) foxHttpClient.getFoxHttpResponseParser().serializedToObject(getStringBody(), (Class<Serializable>) parseClass);
        } catch (IOException e) {
            throw new FoxHttpResponseException(e);
        }
    }

    /**
     * Returns a readable summary uf the response
     *
     * @param showBody should the response body be included
     * @return summary uf the response
     */
    public String toString(boolean showBody) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("======= Request =======\n");
        stringBuilder.append("Request-URL: ").append(foxHttpRequest.getUrl().toString()).append("\n");
        stringBuilder.append("Request-Method: ").append(foxHttpRequest.getRequestType()).append("\n");
        stringBuilder.append("Request-Header: ").append(foxHttpRequest.getRequestHeader()).append("\n");
        if (foxHttpRequest.getRequestType() == RequestType.POST || foxHttpRequest.getRequestType() == RequestType.PUT) {
            stringBuilder.append("Request-Body: ").append(foxHttpRequest.getRequestBody()).append("\n");
        }
        stringBuilder.append("\n");
        stringBuilder.append("======= Response =======\n");
        stringBuilder.append("Response-Code: ").append(responseCode);
        try {
            stringBuilder.append(" ").append(((HttpURLConnection) foxHttpRequest.getConnection()).getResponseMessage());
        } catch (IOException e) {
        }
        stringBuilder.append("\n");
        stringBuilder.append("Response-Headers: ").append(responseHeaders).append("\n");
        if (showBody) {
            stringBuilder.append("Response-Body: \n");
            try {
                stringBuilder.append(getStringBody()).append("\n");
            } catch (IOException e) {
                stringBuilder.append("null (IOException)").append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
