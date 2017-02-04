package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The annotation @HeaderField is one record of the request header which is dynamically (the value) defined.
 * Can be used multiple times.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderField {
    String value();
}
