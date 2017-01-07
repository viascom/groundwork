package ch.viascom.groundwork.foxhttp.annotation.processor;

import ch.viascom.groundwork.foxhttp.annotation.types.*;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.body.request.RequestMultipartBody;
import ch.viascom.groundwork.foxhttp.body.request.RequestUrlEncodedFormBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpRequestHeader;
import ch.viascom.groundwork.foxhttp.header.HeaderEntry;
import ch.viascom.groundwork.foxhttp.query.FoxHttpRequestQuery;
import ch.viascom.groundwork.foxhttp.util.NamedInputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
class FoxHttpAnnotationRequestBuilder {

    static Map<String, String> getPathValues(Method method, Object[] args) {
        Map<String, String> pathValues = new HashMap<>();

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Path) {

                    pathValues.put(((Path) annotation).value(), (String) args[parameterPos]);
                }
            }
            parameterPos++;
        }

        return pathValues;
    }

    @SuppressWarnings("unchecked")
    static FoxHttpRequestQuery getFoxHttpRequestQuery(Method method, Object[] args) {
        FoxHttpRequestQuery foxHttpRequestQuery = new FoxHttpRequestQuery();

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Query) {
                    foxHttpRequestQuery.addQueryEntry(((Query) annotation).value(), (String) args[parameterPos]);
                } else if (annotation instanceof QueryMap) {
                    foxHttpRequestQuery.addQueryMap((HashMap<String, String>) args[parameterPos]);
                }
            }
            parameterPos++;
        }

        return foxHttpRequestQuery;
    }

    @SuppressWarnings("unchecked")
    static FoxHttpRequestHeader setFoxHttpRequestHeader(FoxHttpRequestHeader foxHttpRequestHeader, Method method, Object[] args) throws FoxHttpRequestException {

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof HeaderField) {
                    foxHttpRequestHeader.addHeader(((HeaderField) annotation).value(), (String) args[parameterPos]);
                } else if (annotation instanceof HeaderFieldMap) {
                    if (args[parameterPos] instanceof Map) {
                        foxHttpRequestHeader.addHeader((Map<String, String>) args[parameterPos]);
                    } else if (args[parameterPos] instanceof List) {
                        foxHttpRequestHeader.addHeader((ArrayList<HeaderEntry>) args[parameterPos]);
                    } else {
                        throw new FoxHttpRequestException("@HeaderFieldMap annotation does not support " + args[parameterPos].getClass());
                    }

                }
            }
            parameterPos++;
        }

        return foxHttpRequestHeader;
    }

    static FoxHttpRequestBody getFoxHttpRequestBody(Method method, Object[] args) throws FileNotFoundException, FoxHttpRequestException {
        FoxHttpRequestBody foxHttpRequestBody;

        if (FoxHttpAnnotationUtil.hasMethodAnnotation(MultipartBody.class, method)) {
            //@MultipartBody
            foxHttpRequestBody = getRequestMultipartBody(method, args);
        } else if (FoxHttpAnnotationUtil.hasMethodAnnotation(FormUrlEncodedBody.class, method)) {
            //@FormUrlEncodedBody
            foxHttpRequestBody = getRequestUrlEncodedFormBody(method, args);
        } else if (FoxHttpAnnotationUtil.hasParameterAnnotation(Body.class, method)) {
            //@Body
            foxHttpRequestBody = getRequestBody(method, args);
        } else {
            foxHttpRequestBody = null;
        }
        return foxHttpRequestBody;
    }

    private static FoxHttpRequestBody getRequestBody(Method method, Object[] args) {
        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Body) {
                    return (FoxHttpRequestBody) args[parameterPos];
                }
            }
            parameterPos++;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static RequestMultipartBody getRequestMultipartBody(Method method, Object[] args) throws FileNotFoundException, FoxHttpRequestException {
        MultipartBody multipartBody = method.getAnnotation(MultipartBody.class);

        RequestMultipartBody requestMultipartBody = new RequestMultipartBody(Charset.forName(multipartBody.charset()), multipartBody.linefeed());

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Part) {
                    Part part = (Part) annotation;

                    if (args[parameterPos] instanceof File) {
                        requestMultipartBody.addFilePart(part.value(), (File) args[parameterPos]);
                    } else if (args[parameterPos] instanceof String) {
                        requestMultipartBody.addFormField(part.value(), (String) args[parameterPos]);
                    } else if (args[parameterPos] instanceof NamedInputStream) {
                        NamedInputStream namedInputStream = (NamedInputStream) args[parameterPos];
                        requestMultipartBody.addInputStreamPart(part.value(),
                                namedInputStream.getName(),
                                namedInputStream.getInputStream(),
                                namedInputStream.getContentTransferEncoding(),
                                namedInputStream.getType()
                        );
                    } else {
                        throw new FoxHttpRequestException("@Part annotation does not support " + args[parameterPos].getClass());
                    }
                } else if (annotation instanceof PartMap) {
                    PartMap partMap = (PartMap) annotation;
                    if (partMap.isStreamMap()) {
                        requestMultipartBody.getStream().putAll((HashMap<String, NamedInputStream>) args[parameterPos]);
                    } else {
                        requestMultipartBody.getForms().putAll((HashMap<String, String>) args[parameterPos]);
                    }
                }
            }
            parameterPos++;
        }

        return requestMultipartBody;
    }

    @SuppressWarnings("unchecked")
    private static RequestUrlEncodedFormBody getRequestUrlEncodedFormBody(Method method, Object[] args) {
        RequestUrlEncodedFormBody requestUrlEncodedFormBody = new RequestUrlEncodedFormBody();

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Field) {
                    Field field = (Field) annotation;
                    requestUrlEncodedFormBody.addFormEntry(field.value(), (String) args[parameterPos]);
                } else if (annotation instanceof FieldMap) {
                    requestUrlEncodedFormBody.addFormMap((HashMap<String, String>) args[parameterPos]);
                }
            }
            parameterPos++;
        }

        return requestUrlEncodedFormBody;
    }
}
