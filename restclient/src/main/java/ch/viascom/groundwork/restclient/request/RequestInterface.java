package ch.viascom.groundwork.restclient.request;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.filter.RESTFilter;
import ch.viascom.groundwork.restclient.response.generic.Response;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Request interface for all request
 *
 * @author patrick.boesch@viascom.ch
 */
public interface RequestInterface<T extends Response> {

    /**
     * Get the url of the request
     *
     * Examples:
     * - http://rest.viascom.ch:8000
     * - https://127.0.0.1:8080
     *
     * @return A url
     */
    String getUrl();

    /**
     * Set url of the request
     *
     * Examples:
     * - http://rest.viascom.ch:8000
     * - https://127.0.0.1:8080
     *
     * @param url url to the rest endpoint
     */
    void setUrl(String url);

    /**
     * Set path of the request
     *
     * Examples:
     * - /post/1
     * - /user/12/address
     *
     * @return path of the request
     */
    String getPath();

    /**
     * Get path of the request
     *
     * Examples:
     * - /post/1
     * - /user/12/address
     *
     * @param path path to the rest endpoint
     */
    void setPath(String path);

    /**
     * Get media type (Content-Type property) of the request
     *
     * @return media type
     */
    String getMediaType();

    /**
     * Set media type (Content-Type property) of the request
     *
     * @param mediaType media type
     */
    void setMediaType(String mediaType);

    /**
     * Get the query map of the request
     *
     * key: property name
     * value: property value
     *
     * Example:
     * {"definitionId": "12974", "active": "true"} => ?definitionId=12974&active=true
     *
     * @return query map of the request
     */
    HashMap<String, String> getQueryParamMap();

    /**
     * Set the query map of the request
     *
     * key: property name
     * value: property value
     *
     * Example:
     * {"definitionId": "12974", "active": "true"} => ?definitionId=12974&active=true
     *
     * @param queryParamMap hash map containing the query properties
     */
    void setQueryParamMap(HashMap<String, String> queryParamMap);

    /**
     * Set query map of the request based on a objects and its attributes
     *
     * @param params list of attribute names which are used for the query map
     * @param o object with the attributes
     * @throws RESTClientException
     */
    void setRequestParams(ArrayList<String> params, Object o) throws RESTClientException;

    /**
     * Add a new query property
     *
     * @param name property name
     * @param value propert value
     */
    void addQueryParam(String name, String value);

    /**
     * Get the header map of the request
     *
     * key: property name
     * value: property value
     *
     * Example:
     * {"Accept-Charset": "UTF-8"}
     *
     * @return header map of the request
     */
    HashMap<String, String> getHeaderMap();

    /**
     * Set the header map of the request
     *
     * key: property name
     * value: property value
     *
     * Example:
     * {"Accept-Charset": "UTF-8"}
     *
     * @param headerMap hash map containing the header properties
     */
    void setHeaderMap(HashMap<String, String> headerMap);

    /**
     * Add a new header property
     *
     * @param name property name
     * @param value propert value
     */
    void addHeaders(String name, String value);

    /**
     * Register a RESTFilters for the Request/Response
     *
     * @param restFilter RESTFilter for the request
     */
    void register(RESTFilter restFilter);

    /**
     * Unregister a RESTFilters for the Request/Response
     *
     * @param restFilter Class of a restFilter for the request
     */
    void unregister(Class restFilter);

    /**
     * Get the RESTFilters for the Request/Response
     *
     * @return Array of RESTFilters of the request
     */
    HashMap<String, Object> getRESTFilters();

    /**
     * Get the RESTFilters of a specific type for the Request/Response
     *
     * @return Array of RESTFilters of the request
     */
    ArrayList<RESTFilter> getRESTFilters(FilterTypes filterType);

    /**
     * Set additional allowed status codes
     *
     * <i>this will extend the default of statuscode >= 200 && < 300</i>
     *
     * @param additionalAllowedStatusCodes Array of additional allowed status codes
     */
    void setAdditionalAllowedStatusCodes(ArrayList<Integer> additionalAllowedStatusCodes);

    /**
     * Get all additional allowed status codes
     *
     * @return additional allowed status codes
     */
    ArrayList<Integer> getAdditionalAllowedStatusCodes();

    /**
     * Add an additional allowed status code
     *
     * @param code status code to allow
     */
    void addAdditionalAllowedStatusCode(int code);

    /**
     * Set additional denied status codes
     *
     * <i>this will override the default of statuscode >= 200 && < 300</i>
     *
     * @param additionalDeniedStatusCodes Array of additional denied status codes
     */
    void setAdditionalDeniedStatusCodes(ArrayList<Integer> additionalDeniedStatusCodes);

    /**
     * Get all additional denied status codes
     *
     * @return additional denied status codes
     */
    ArrayList<Integer> getAdditionalDeniedStatusCodes();

    /**
     * Add an denied status code
     *
     * @param code status code to denie
     */
    void addAdditionalDeniedStatusCode(int code);

    /**
     * Generate request url with all informations for the request
     *
     * @throws Exception
     */
    void prepareQuery() throws Exception;

    /**
     * Send request to the rest endpoint
     *
     * @return Http response of the request
     * @throws Exception
     */
    Object request() throws Exception;

    /**
     * Executes the request and returns a deserialized object of the response based on T
     *
     * @return deserialized object of the response based on T
     * @throws Exception
     */
    T execute() throws Exception;

    /**
     * Get the generic class T
     *
     * @return generic class T
     */
    Class<T> getParameterClass();
}
