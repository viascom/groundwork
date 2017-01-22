package ch.viascom.groundwork.foxauth.validator;

import ch.viascom.groundwork.foxauth.FoxAuth;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxAuthValidator {
    boolean validate(ContainerRequestContext containerRequestContext, FoxAuth foxAuth);
}
