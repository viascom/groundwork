package ch.viascom.groundwork.foxhttp.response.serviceresult;

import java.lang.annotation.*;

/**
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceResult {
    boolean value() default false;
}
