package ch.viascom.groundwork.foxauth.interceptor;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.annotation.FoxAuthorizationEndpoint;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.response.error.FoxAuthErrorResponse;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidator;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

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
@FoxAuthorizationEndpoint
@Priority(Priorities.AUTHENTICATION)
public class FoxAuthorizationEndpointInterceptor implements ContainerRequestFilter {

    public FoxAuthorizationEndpointInterceptor() {
        System.out.println("-> Call method FoxAuthorizationEndpointInterceptor:FoxAuthorizationEndpointInterceptor");
    }

    @Inject
    FoxAuth foxAuth;

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        try {
            HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(request);

            FoxAuthValidator foxAuthValidator = foxAuth.getFoxAuthValidatorStrategy().getValidator(AuthValidatorType.OAUTH2_AUTHORIZATION_CODE_FLOW);

            FoxAuthValidation foxAuthValidation = new FoxAuthValidation();

            foxAuthValidation = foxAuthValidator.validate(new FoxAuthHttpServletRequestWrapper(httpServletRequestWrapper), foxAuth, foxAuthValidation);
            request.setAttribute("ch.viascom.groundwork.foxauth.foxAuthValidation", foxAuthValidation);

            if (!foxAuthValidation.isStatus()) {
                FoxAuthErrorResponse foxAuthErrorResponse = foxAuth.getFoxAuthErrorResponse();
                foxAuthErrorResponse.setError(foxAuthValidation.getErrorCode());
                foxAuthErrorResponse.setError_description(foxAuthValidation.getErrorMessage());
                foxAuthErrorResponse.setError_uri(foxAuthValidation.getErrorUrl());
                if (foxAuth.isAutoResponseOnError()) {
                    requestContext.abortWith(Response.status(Response.Status.FOUND).entity(foxAuthErrorResponse).build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FoxAuthValidation foxAuthValidation = new FoxAuthValidation(false, null, "", "", "", null, null, e.getMessage(), "", "");
            request.setAttribute("ch.viascom.groundwork.foxauth.foxAuthValidation", foxAuthValidation);
        }
    }
}
