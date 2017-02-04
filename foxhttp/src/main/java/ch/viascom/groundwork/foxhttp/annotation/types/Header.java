package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The annotation @Header is one record of the request header which is fixed defined.
 * Can be used multiple times.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Header {
    String name();
    String value();
}
