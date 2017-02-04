package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The annotation @Body defines the the parameter which represents the body of the request.
 * If no parameter is annotated with @Body the request will be send without a body.
 * The annotated parameter has to be assignable from {@link ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody
 * FoxHttpRequestBody}
 *
 * This annotation can only be used in POST and PUT requests otherwise a
 * {@link ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException FoxHttpRequestException} will be thrown.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Body {
}
