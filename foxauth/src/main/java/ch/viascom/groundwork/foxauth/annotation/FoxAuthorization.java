package ch.viascom.groundwork.foxauth.annotation;

import javax.ws.rs.NameBinding;
import java.lang.annotation.*;

/**
 * @author patrick.boesch@viascom.ch
 */
@NameBinding
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FoxAuthorization {
}
