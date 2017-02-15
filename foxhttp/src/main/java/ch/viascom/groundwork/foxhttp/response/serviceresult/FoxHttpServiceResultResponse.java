package ch.viascom.groundwork.foxhttp.response.serviceresult;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.body.response.FoxHttpResponseBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.foxhttp.response.FoxHttpResponseParser;
import ch.viascom.groundwork.serviceresult.ServiceResult;
import ch.viascom.groundwork.serviceresult.ServiceResultStatus;
import ch.viascom.groundwork.serviceresult.exception.ServiceFault;
import ch.viascom.groundwork.serviceresult.util.Metadata;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
public class FoxHttpServiceResultResponse implements FoxHttpResponseParser {

    private ServiceResultStatus status;
    private String type;
    @Getter(AccessLevel.PRIVATE)
    private Serializable content;
    private String hash;
    private String destination;
    private HashMap metadata = new HashMap<>();

    private FoxHttpResponseBody responseBody = new FoxHttpResponseBody();
    private int responseCode = -1;
    private FoxHttpHeader responseHeaders;
    private FoxHttpClient foxHttpClient;
    private FoxHttpRequest foxHttpRequest;

    private Gson parser;
    private FoxHttpServiceResultHasher objectHasher;


    /**
     * Create a new FoxHttpServiceResultParser
     *
     * @param foxHttpResponse response with a serialized service result
     * @param customParser    a custom gson parser
     */
    public FoxHttpServiceResultResponse(FoxHttpResponse foxHttpResponse, Gson customParser) throws FoxHttpResponseException {
        this(foxHttpResponse, null, customParser);
    }

    /**
     * Create a new FoxHttpServiceResultParser
     *
     * @param foxHttpResponse response with a serialized service result
     */
    public FoxHttpServiceResultResponse(FoxHttpResponse foxHttpResponse) throws FoxHttpResponseException {
        this(foxHttpResponse, null, null);
    }

    /**
     * Create a new FoxHttpServiceResultParser
     *
     * @param foxHttpResponse response with a serialized service result
     * @param objectHasher    object hasher to check the result
     */
    public FoxHttpServiceResultResponse(FoxHttpResponse foxHttpResponse, FoxHttpServiceResultHasher objectHasher) throws FoxHttpResponseException {
        this(foxHttpResponse, objectHasher, null);
    }

    /**
     * Create a new FoxHttpServiceResultParser
     *
     * @param foxHttpResponse response with a serialized service result
     * @param objectHasher    object hasher to check the result
     * @param customParser    a custom gson parser
     */
    public FoxHttpServiceResultResponse(FoxHttpResponse foxHttpResponse, FoxHttpServiceResultHasher objectHasher, Gson customParser) throws FoxHttpResponseException {
        this.responseBody = foxHttpResponse.getResponseBody();
        this.foxHttpClient = foxHttpResponse.getFoxHttpClient();
        this.responseCode = foxHttpResponse.getResponseCode();
        this.foxHttpRequest = foxHttpResponse.getFoxHttpRequest();
        this.responseHeaders = foxHttpResponse.getResponseHeaders();
        this.objectHasher = objectHasher;

        if (customParser == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Metadata.class, new MetaDataDeserializer());
            this.parser = gsonBuilder.create();
        } else {
            this.parser = customParser;
        }

        try {
            String body = getStringBody();
            ServiceResult result = parser.fromJson(body, ServiceResult.class);
            this.type = result.getType();
            this.hash = result.getHash();
            this.destination = result.getDestination();
            this.metadata = result.getMetadata();
            this.status = result.getStatus();
        } catch (IOException e) {
            throw new FoxHttpResponseException(e);
        }

        foxHttpClient.getFoxHttpLogger().log("FoxHttpServiceResultParser(" + foxHttpResponse + "," + objectHasher + ")");
    }

    /**
     * Constructor for annotation use
     *
     * @param objectHasher object hasher to check the result
     */
    public FoxHttpServiceResultResponse(FoxHttpServiceResultHasher objectHasher) {
        this.objectHasher = objectHasher;
    }

    /**
     * Parser for annotation use
     *
     * @param foxHttpResponse response with a serialized service result
     * @return new FoxHttpResponseParser
     * @throws FoxHttpResponseException
     */
    public FoxHttpResponseParser parseResult(FoxHttpResponse foxHttpResponse) throws FoxHttpResponseException {
        return new FoxHttpServiceResultResponse(foxHttpResponse, objectHasher);
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
        BufferedReader rd = new BufferedReader(new InputStreamReader(getInputStreamBody()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\n');
        }
        rd.close();
        return response.toString();
    }


    /**
     * Get the content of the service result
     *
     * @param contentClass class of the return object
     * @return deserialized content of the service result
     * @throws FoxHttpResponseException Exception during the deserialization
     */
    public <T extends Serializable> T getContent(Class<T> contentClass) throws FoxHttpResponseException {
        return getContent(contentClass, false);
    }

    /**
     * Get the content of the service result
     *
     * @param <T> Type of the content
     * @return deserialized content of the service result
     * @throws FoxHttpResponseException Exception during the deserialization
     */
    public <T extends Serializable> T getContentFromType() throws FoxHttpResponseException {
        try {
            return getContent((Class<T>) Class.forName(this.type), false);
        } catch (FoxHttpResponseException e) {
            throw e;
        } catch (Exception e) {
            throw new FoxHttpResponseException(e);
        }

    }

    /**
     * Get the content of the service result
     *
     * @param checkHash should the result be checked
     * @param <T>       Type of the content
     * @return deserialized content of the service result
     * @throws FoxHttpResponseException Exception during the deserialization
     */
    public <T extends Serializable> T getContentFromType(boolean checkHash) throws FoxHttpResponseException {
        try {
            return getContent((Class<T>) Class.forName(this.type), checkHash);
        } catch (FoxHttpResponseException e) {
            throw e;
        } catch (Exception e) {
            throw new FoxHttpResponseException(e);
        }

    }

    /**
     * Get the content of the service result
     *
     * @param contentClass class of the return object
     * @param checkHash    should the result be checked
     * @return deserialized content of the service result
     * @throws FoxHttpResponseException Exception during the deserialization
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getContent(Class<T> contentClass, boolean checkHash) throws FoxHttpResponseException {
        try {

            Type parameterizedType = new ServiceResultParameterizedType(contentClass);

            String body = getStringBody();

            ServiceResult<T> result = parser.fromJson(body, parameterizedType);
            foxHttpClient.getFoxHttpLogger().log("processServiceResult(" + result + ")");
            this.content = result.getContent();

            checkHash(checkHash, body, result);

            return (T) this.content;
        } catch (IOException e) {
            throw new FoxHttpResponseException(e);
        }

    }

    private void checkHash(boolean checkHash, String body, ServiceResult<?> result) throws FoxHttpResponseException {
        if (checkHash && objectHasher != null) {
            foxHttpClient.getFoxHttpLogger().log("checkHash(" + result.getHash() + ")");
            if (!objectHasher.hash(result, body).equals(result.getHash())) {
                throw new FoxHttpResponseException("Hash not Equal!");
            }
            foxHttpClient.getFoxHttpLogger().log("-> successful");
        }
    }

    /**
     * Get the fault of the service result
     *
     * @return deserialized fault of the service result
     * @throws FoxHttpResponseException Exception during the deserialization
     */
    public ServiceFault getFault() throws FoxHttpResponseException {
        return getFault(false);
    }

    /**
     * Get the fault of the service result
     *
     * @param checkHash should the result be checked
     * @return deserialized fault of the service result
     * @throws FoxHttpResponseException Exception during the deserialization
     */
    public ServiceFault getFault(boolean checkHash) throws FoxHttpResponseException {
        try {

            String body = getStringBody();

            ServiceResult<ServiceFault> result = parser.fromJson(body, new TypeToken<ServiceResult<ServiceFault>>() {
            }.getType());
            foxHttpClient.getFoxHttpLogger().log("processFault(" + result + ")");

            checkHash(checkHash, body, result);

            return result.getContent();
        } catch (IOException e) {
            throw new FoxHttpResponseException(e);
        }

    }
}
