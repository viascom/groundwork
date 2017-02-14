package ch.viascom.groundwork.foxhttp.interfaces;

import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.annotation.types.Body;
import ch.viascom.groundwork.foxhttp.annotation.types.GET;
import ch.viascom.groundwork.foxhttp.body.request.RequestStringBody;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpExceptionInterfaceTest {

    @GET("get")
    FoxHttpResponse bodyInGet(@Body RequestStringBody body);
}
