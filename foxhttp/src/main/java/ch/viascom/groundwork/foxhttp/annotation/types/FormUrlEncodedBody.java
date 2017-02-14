package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The @FormUrlEncodedBody annotation defines that the request uses a form-url-encoded body.
 *
 * The Form-encoded method must contain at least one @Field from type {@link java.lang.String String} or @FieldMap from type
 * {@link java.util.Map Map} and cannot contain the annotations @Body, @Part or @PartMap.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormUrlEncodedBody {
}
