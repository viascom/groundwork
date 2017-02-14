package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The annotation @FollowRedirect defines if the request should follow redirects.
 * The default if this annotation is not used is set to true.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FollowRedirect {
    boolean value();
}
