package ch.viascom.foxauth;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.annotation.FoxAuthorization;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * @author patrick.boesch@viascom.ch
 */
@Path("/rest/")
public class RESTService {

    @GET
    @Path("get")
    public String get(@Context HttpServletRequest request) {

        /*FoxAuth foxAuth = new FoxAuth();
        foxAuth.enableOAuth2();
        foxAuth.setFoxAuthDataValidator();

        FoxAuthValidation foxAuthValidation = foxAuth.validate(request);
        if (foxAuthValidation.isStatus()) {
            return "OK !";
        } else {
            return "NoAuth!";
        }
*/
        return "OK !";
    }

    @POST
    @Path("post")
    @Produces("application/json")
    @FoxAuthorization
    public String post(@Context HttpServletRequest request) {

        FoxAuthValidation foxAuthValidation = FoxAuth.getFoxAuthValidationFromInterceptor(request);

        System.out.println(foxAuthValidation);
        return "OK !";
    }
}
