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
import java.nio.charset.Charset;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@NoArgsConstructor
public class FoxHttpResponse {

    private FoxHttpResponseBody responseBody = new FoxHttpResponseBody();

    private int responseCode = -1;

    private FoxHttpHeader responseHeaders;

    private FoxHttpClient foxHttpClient;

    private FoxHttpRequest foxHttpRequest;

    public FoxHttpResponse(InputStream body, FoxHttpRequest foxHttpRequest, int responseCode, FoxHttpClient foxHttpClient) throws IOException, FoxHttpException {
        this.responseBody.setBody(body);
        foxHttpClient.getFoxHttpLogger().log("setResponseBody(" + getStringBody() + ")");
        this.foxHttpClient = foxHttpClient;
        this.responseCode = responseCode;
        this.foxHttpRequest = foxHttpRequest;
        //Execute interceptor
        foxHttpClient.getFoxHttpLogger().log("executeResponseBodyInterceptor()");
        FoxHttpInterceptorExecutor.executeResponseBodyInterceptor(
                new FoxHttpResponseBodyInterceptorContext(responseCode, this, foxHttpRequest, foxHttpClient)
        );
    }

    /**
     * Get the body as input stream
     *
     * @return body as input stream
     */
    public InputStream getInputStreamBody() {
        return new ByteArrayInputStream(responseBody.getBody().toByteArray());
    }

    /**
     * Get the body as output stream
     *
     * @return body as output stream
     */
    public ByteArrayOutputStream getByteArrayOutputStreamBody() {
        return responseBody.getBody();
    }

    protected void setBody(InputStream body) throws IOException {
        this.responseBody.setBody(body);
    }

    /**
     * Get the response body as string
     *
     * @return body as string
     * @throws IOException if the stream is not accessible
     */
    public String getStringBody() throws IOException {
        return getStringBody(Charset.defaultCharset());
    }

    /**
     * Get the response body as string
     *
     * @param charset charset for the byte to String conversion
     * @return body as string
     * @throws IOException if the stream is not accessible
     */
    public String getStringBody(Charset charset) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(getInputStreamBody(), charset));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            if (response.length() != 0) {
                response.append('\n');
            }
            response.append(line);
        }
        rd.close();
        return response.toString();
    }

    /**
     * Get the parsed result
     * <i>uses the response parser of the FoxHttpClient</i>
     *
     * @param parseClass class of the return object
     * @return deserialized result
     * @throws FoxHttpResponseException Exception during the deserialization
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getParsedBody(Class<T> parseClass) throws FoxHttpResponseException {
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
            stringBuilder.append(" [couldn't load ResponseMessage]");
        }
        stringBuilder.append("\n");
        stringBuilder.append("Response-Headers: ").append(responseHeaders).append("\n");
        if (showBody) {
            stringBuilder.append("Response-Body: \n");
            try {
                stringBuilder.append(getStringBody()).append("\n");
            } catch (IOException e) {
                stringBuilder.append("null (").append(e.getMessage()).append(")").append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
