package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The @MultipartBody annotation defines that the request uses a multipart body.
 *
 * The Form-encoded method must contain at least one @Part or @PartMap from type
 * {@link java.util.Map Map} and cannot contain the annotations @Body, @Field or @FieldMap.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartBody {
    String charset() default "UTF-8";
    String linefeed() default "\n";
}
