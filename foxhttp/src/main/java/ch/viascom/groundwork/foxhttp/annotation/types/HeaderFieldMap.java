package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The annotation @HeaderFieldMap is a collection of {@link ch.viascom.groundwork.foxhttp.annotation.types.HeaderField HeaderField's}.
 * Can be used multiple times.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderFieldMap {
}
