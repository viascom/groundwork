package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The @QueryMap annotation defines a collection of {@link ch.viascom.groundwork.foxhttp.annotation.types.Query Query's}
 * used as part of the URL path.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryMap {
}
