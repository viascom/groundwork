package ch.viascom.groundwork.foxauth.interceptor;


import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.annotation.FoxAuthorization;
import ch.viascom.groundwork.foxauth.response.error.FoxAuthErrorResponse;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author patrick.boesch@viascom.ch
 */
@Provider
@FoxAuthorization
@Priority(Priorities.AUTHENTICATION)
public class FoxAuthRequestInterceptor implements ContainerRequestFilter {

    public FoxAuthRequestInterceptor() {
        System.out.println("-> Call method FoxAuthRequestInterceptor:FoxAuthRequestInterceptor");
    }

    @Inject
    FoxAuth foxAuth;

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        System.out.println("-> Call method FoxAuthRequestInterceptor:filter");
        HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(request);

        FoxAuthValidation foxAuthValidation = foxAuth.validate(httpServletRequestWrapper);

        request.setAttribute("ch.viascom.groundwork.foxauth.foxAuthValidation", foxAuthValidation);
        if (!foxAuthValidation.isStatus()) {
            FoxAuthErrorResponse foxAuthErrorResponse = foxAuth.getFoxAuthErrorResponse();
            foxAuthErrorResponse.setError(foxAuthValidation.getErrorCode());
            foxAuthErrorResponse.setError_description(foxAuthValidation.getErrorMessage());
            foxAuthErrorResponse.setError_uri(foxAuthValidation.getErrorUrl());
            if (foxAuth.isAutoResponseOnError()) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(foxAuthErrorResponse).build());
            }
        }
    }
}
