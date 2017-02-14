package ch.viascom.groundwork.foxhttp.annotation.processor;

import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.response.FoxHttpResponseParser;
import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class FoxHttpAnnotationInvocationHandler implements InvocationHandler {

    private HashMap<Method, FoxHttpRequestBuilder> requestCache = new HashMap<>();
    private HashMap<Class<? extends Annotation>, FoxHttpResponseParser> responseParsers = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws FoxHttpException {
        try {
            //pass through class methods
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }

            FoxHttpRequest request = requestCache.get(method).build();

            //Resolve path
            request.getFoxHttpClient().getFoxHttpPlaceholderStrategy().getPlaceholderMap().putAll(FoxHttpAnnotationRequestBuilder.getPathValues(method, args));
            //Set query
            request.setRequestQuery(FoxHttpAnnotationRequestBuilder.getFoxHttpRequestQuery(method, args));
            //Set headers
            FoxHttpAnnotationRequestBuilder.setFoxHttpRequestHeader(request.getRequestHeader(), method, args);
            //Set Body
            FoxHttpRequestBody foxHttpRequestBody = FoxHttpAnnotationRequestBuilder.getFoxHttpRequestBody(method, args);
            if (foxHttpRequestBody != null) {
                request.setRequestBody(foxHttpRequestBody);
            }

            //Prepare return value
            if (method.getReturnType().isAssignableFrom(FoxHttpResponse.class)) {
                return request.execute();
            } else if (method.getReturnType().isAssignableFrom(FoxHttpRequest.class)) {
                return request;
            } else if (method.getReturnType().isAssignableFrom(String.class)) {
                return request.execute().getStringBody();
            } else if (method.getReturnType().isAssignableFrom(ByteArrayOutputStream.class)) {
                return request.execute().getByteArrayOutputStreamBody();
            } else if (method.getReturnType().isAssignableFrom(InputStream.class)) {
                return request.execute().getInputStreamBody();
            } else {
                //Search for registered response parser
                for (Map.Entry<Class<? extends Annotation>, FoxHttpResponseParser> entry : responseParsers.entrySet()) {
                    if (FoxHttpAnnotationUtil.hasMethodAnnotation(entry.getKey(), method)) {
                        if (method.getReturnType().isAssignableFrom(entry.getValue().getClass())) {
                            return entry.getValue().parseResult(request.execute());
                        } else {
                            Method valueMethod = method.getAnnotation(entry.getKey()).getClass().getMethod("value");
                            boolean checkHash = (boolean) valueMethod.invoke(method.getAnnotation(entry.getKey()));
                            return entry.getValue().parseResult(request.execute())
                                    .getContent((Class<Serializable>) method.getReturnType(), checkHash);
                        }
                    }
                }

                //Return as parsed object if nothing else matches
                Class<Serializable> serializableClass = (Class<Serializable>) method.getReturnType();
                return request.execute().getParsedBody(serializableClass);
            }
        } catch (FoxHttpException e) {
            throw e;
        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
        }
    }
}
