package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderField {
    String value();
}
