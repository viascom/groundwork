package ch.viascom.groundwork.foxauth.annotation;

import javax.ws.rs.NameBinding;
import java.lang.annotation.*;

/**
 * @author patrick.boesch@viascom.ch
 */
@NameBinding
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FoxTokenEndpoint {
}
