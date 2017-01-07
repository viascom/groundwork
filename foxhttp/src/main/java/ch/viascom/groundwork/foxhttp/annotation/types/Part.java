package ch.viascom.groundwork.foxhttp.annotation.types;

/**
 * @author patrick.boesch@viascom.ch
 */

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Part {
    String value();
}
