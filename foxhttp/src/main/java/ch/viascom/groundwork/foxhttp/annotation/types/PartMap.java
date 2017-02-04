package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 *
 * Can only be used in multipart requests.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PartMap {
    boolean isStreamMap() default false;
}
