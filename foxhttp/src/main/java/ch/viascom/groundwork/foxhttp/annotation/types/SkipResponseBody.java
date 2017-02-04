package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The @SkipResponseBody annotation will skip the response body if set to true. The default is false.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipResponseBody {
    boolean value();
}
