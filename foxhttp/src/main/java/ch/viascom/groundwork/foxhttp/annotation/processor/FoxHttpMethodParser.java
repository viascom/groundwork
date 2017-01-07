package ch.viascom.groundwork.foxhttp.annotation.processor;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.annotation.types.*;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpRequestHeader;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static ch.viascom.groundwork.foxhttp.annotation.processor.FoxHttpAnnotationUtil.*;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
class FoxHttpMethodParser {

    private Method method;
    private String path;
    private URL url;
    private RequestType requestType;
    private boolean hasBody;
    private Class<?> responseType;
    private FoxHttpClient foxHttpClient;
    private FoxHttpRequestHeader headerFields = new FoxHttpRequestHeader();
    private boolean skipResponseBody = false;
    private boolean followRedirect = true;


    void parseMethod(Method method, FoxHttpClient foxHttpClient) throws FoxHttpRequestException {

        this.method = method;
        this.foxHttpClient = foxHttpClient;

        parseClassHeaders();

        parseReturnType(method.getReturnType());

        parseSkipResponseBodyAndFollowRedirect();

        for (Annotation annotation : method.getAnnotations()) {
            parsetMethodAnnotation(annotation);
        }

        parseURL();

    }

    private void parseClassHeaders() throws FoxHttpRequestException {

        for (Annotation annotation : method.getDeclaringClass().getAnnotations()) {
            if (annotation instanceof Header) {
                setHeader((Header) annotation);
            }
        }
    }

    private void parseSkipResponseBodyAndFollowRedirect() {
        //From class
        SkipResponseBody skipResponseBodyAnnotation = method.getDeclaringClass().getAnnotation(SkipResponseBody.class);
        if (skipResponseBodyAnnotation != null) {
            skipResponseBody = skipResponseBodyAnnotation.value();
        }

        FollowRedirect followRedirectAnnotation = method.getDeclaringClass().getAnnotation(FollowRedirect.class);
        if (followRedirectAnnotation != null) {
            followRedirect = followRedirectAnnotation.value();
        }

        //From method
        skipResponseBodyAnnotation = method.getAnnotation(SkipResponseBody.class);
        if (skipResponseBodyAnnotation != null) {
            skipResponseBody = skipResponseBodyAnnotation.value();
        }

        followRedirectAnnotation = method.getAnnotation(FollowRedirect.class);
        if (followRedirectAnnotation != null) {
            followRedirect = followRedirectAnnotation.value();
        }
    }

    private void parseURL() throws FoxHttpRequestException {
        Path basePath = method.getDeclaringClass().getAnnotation(Path.class);

        String url = path;
        if (basePath != null) {
            url = basePath.value() + url;
        }

        for (Map.Entry<String, String> entry : foxHttpClient.getFoxHttpPlaceholderStrategy().getPlaceholderMap().entrySet()) {
            url = url.replace(foxHttpClient.getFoxHttpPlaceholderStrategy().getPlaceholderEscapeCharStart()
                    + entry.getKey()
                    + foxHttpClient.getFoxHttpPlaceholderStrategy().getPlaceholderEscapeCharEnd(), entry.getValue());
        }

        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new FoxHttpRequestException(e);
        }
    }

    private void parseReturnType(Class<?> responseType) throws FoxHttpRequestException {
        if ((!responseType.isAssignableFrom(FoxHttpResponse.class) &&
                !responseType.isAssignableFrom(String.class) &&
                !responseType.isAssignableFrom(void.class) &&
                !responseType.isAssignableFrom(FoxHttpRequest.class)) && foxHttpClient.getFoxHttpResponseParser() == null) {
            throwFoxHttpRequestException("The used return type needs a FoxHttpResponseParser to deserialize the body");
        } else {
            this.responseType = responseType;
        }
    }

    private void parsetMethodAnnotation(Annotation annotation) throws FoxHttpRequestException {
        if (annotation instanceof DELETE) {
            setRequestTypeAndUrl("DELETE", ((DELETE) annotation).value(), false);
        } else if (annotation instanceof GET) {
            setRequestTypeAndUrl("GET", ((GET) annotation).value(), false);
        } else if (annotation instanceof HEAD) {
            setRequestTypeAndUrl("HEAD", ((HEAD) annotation).value(), false);
        } else if (annotation instanceof POST) {
            setRequestTypeAndUrl("POST", ((POST) annotation).value(), true);
        } else if (annotation instanceof PUT) {
            setRequestTypeAndUrl("PUT", ((PUT) annotation).value(), true);
        } else if (annotation instanceof OPTIONS) {
            setRequestTypeAndUrl("OPTIONS", ((OPTIONS) annotation).value(), false);
        } else if (annotation instanceof Header) {
            setHeader((Header) annotation);
        }

    }

    private void setHeader(Header annotation) throws FoxHttpRequestException {
        Header header = annotation;
        if (header.name().isEmpty() || header.value().isEmpty()) {
            throwFoxHttpRequestException("@Header annotation is empty.");
        }
        headerFields.addHeader(header.name(), header.value());
    }

    private void setRequestTypeAndUrl(String requestType, String value, boolean hasBody) throws FoxHttpRequestException {
        if (this.requestType != null) {
            throwFoxHttpRequestException("Only one HTTP method is allowed. Found: " + this.requestType + " and " + requestType + ".");
        }

        if (!hasBody && hasBodyAnnotation()) {
            throwFoxHttpRequestException("Non-body HTTP method cannot contain @Body, @Field, @FieldMap, @Part or @PartMap.");
        }

        //Parameters
        doParameterMatch(HeaderField.class, String.class, this.method);
        doParameterMatch(Query.class, String.class, this.method);
        doParameterMatch(QueryMap.class, Map.class, this.method);
        doParameterMatch(Path.class, String.class, this.method);

        //Body
        if (hasBody) {
            if (getParameterAnnotation(Body.class, this.method) > 1) {
                throwFoxHttpRequestException("Only one @Body is allowed.");
            }
            doParameterMatch(Body.class, FoxHttpRequestBody.class, this.method);
        }

        //FormUrlEncodedBody
        if (hasMethodAnnotation(FormUrlEncodedBody.class, this.method)) {
            if (!(hasParameterAnnotation(Field.class, this.method) || hasParameterAnnotation(FieldMap.class, this.method))) {
                throwFoxHttpRequestException("Form-encoded method must contain at least one @Field or @FieldMap.");
            }
            if (hasParameterAnnotation(Part.class, this.method) || hasParameterAnnotation(PartMap.class, this.method)) {
                throwFoxHttpRequestException("Form-encoded method cannot contain @Part or @PartMap.");
            }
            if (hasParameterAnnotation(Body.class, this.method)) {
                throwFoxHttpRequestException("Form-encoded method cannot contain @Body.");
            }
            doParameterMatch(Field.class, String.class, this.method);
            doParameterMatch(FieldMap.class, Map.class, this.method);
        }

        //Multipart
        if (hasMethodAnnotation(MultipartBody.class, this.method)) {
            if (!(hasParameterAnnotation(Part.class, this.method) || hasParameterAnnotation(PartMap.class, this.method))) {
                throwFoxHttpRequestException("Multipart method must contain at least one @Part or @PartMap.");
            }
            if (hasParameterAnnotation(Field.class, this.method) || hasParameterAnnotation(FieldMap.class, this.method)) {
                throwFoxHttpRequestException("Multipart method cannot contain @Field or @FieldMap.");
            }
            if (hasParameterAnnotation(Body.class, this.method)) {
                throwFoxHttpRequestException("Multipart method cannot contain @Body.");
            }
            doParameterMatch(PartMap.class, Map.class, this.method);
        }


        this.requestType = RequestType.valueOf(requestType);
        this.hasBody = hasBody;
        this.path = value;
    }


    private boolean hasBodyAnnotation() {
        return hasParameterAnnotation(Body.class, this.method)
                || hasParameterAnnotation(Field.class, this.method)
                || hasParameterAnnotation(FieldMap.class, this.method)
                || hasParameterAnnotation(Part.class, this.method)
                || hasParameterAnnotation(PartMap.class, this.method);
    }


    private void doParameterMatch(Class<? extends Annotation> annotationClass, Class<?> aClass, Method method) throws FoxHttpRequestException {
        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    if (!aClass.isAssignableFrom(method.getParameterTypes()[parameterPos])) {
                        throwFoxHttpRequestException(aClass.getSimpleName() + " is not assignable from Parameter " + method.getParameterTypes()[parameterPos] +
                                " (" + method.getParameterTypes()[parameterPos].getSimpleName() + ") with annotation @" + annotationClass.getSimpleName());
                    }
                }
            }
            parameterPos++;
        }
    }

    private void throwFoxHttpRequestException(String message) throws FoxHttpRequestException {

        StringBuilder outputMessage = new StringBuilder();

        outputMessage.append(method.getDeclaringClass().getSimpleName());
        outputMessage.append(".");
        outputMessage.append(method.getName());
        outputMessage.append("\n-> ");
        outputMessage.append(message);

        throw new FoxHttpRequestException(outputMessage.toString());
    }

}
