package ch.viascom.groundwork.serviceresult;

import ch.viascom.groundwork.serviceresult.exception.ServiceException;
import ch.viascom.groundwork.serviceresult.exception.ServiceFault;
import ch.viascom.groundwork.serviceresult.util.NameValuePair;
import org.junit.Test;

import java.util.ArrayList;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ServiceExceptionTest {

    @Test
    public void ExceptionWrapping() {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException();

        ServiceFault serviceFault = new ServiceFault();
        serviceFault.setCode("ILLEGAL_ARGUMENT_EXCEPTION");
        serviceFault.setMessage("The argument 'productUD' is unknown");
        serviceFault.setRequestedType(ServiceResult.class);
        serviceFault.setRequestUrl("/data/getUser");
        serviceFault.setResponseStatusCode(500);
        serviceFault.setException(illegalArgumentException.toString());

        NameValuePair nameValuePair_1 = new NameValuePair("userId", "45678");
        NameValuePair nameValuePair_2 = new NameValuePair("productUD", "1234");

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(nameValuePair_1);
        nameValuePairs.add(nameValuePair_2);

        serviceFault.setRequestParams(nameValuePairs);

        ServiceException serviceException = new ServiceException();
        serviceException.setFault(serviceFault);
        serviceException.setException(illegalArgumentException);


        assertThat(serviceException.getFault().getCode()).isEqualTo("ILLEGAL_ARGUMENT_EXCEPTION");
        assertThat(serviceException.getFault().getMessage()).isEqualTo("The argument 'productUD' is unknown");
        assertThat(serviceException.getFault().getRequestUrl()).isEqualTo("/data/getUser");
        assertThat(serviceException.getFault().getResponseStatusCode()).isEqualTo(500);
        assertThat(serviceException.getFault().getRequestParams().get(0).getValue()).isEqualTo("45678");

        ServiceException serviceException_2 = new ServiceException(serviceFault);

        assertThat(serviceException_2.getFault().getCode()).isEqualTo("ILLEGAL_ARGUMENT_EXCEPTION");
        assertThat(serviceException_2.getFault().getMessage()).isEqualTo("The argument 'productUD' is unknown");
        assertThat(serviceException_2.getFault().getRequestUrl()).isEqualTo("/data/getUser");
        assertThat(serviceException_2.getFault().getResponseStatusCode()).isEqualTo(500);
        assertThat(serviceException_2.getFault().getRequestParams().get(0).getValue()).isEqualTo("45678");
    }

    @Test
    public void ExceptionFastWrapping() {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException();

        ServiceException serviceException = new ServiceException("ILLEGAL_ARGUMENT_EXCEPTION", "The argument 'productUD' is unknown");
        serviceException.setException(illegalArgumentException);
        serviceException.setResponseStatusCode(500);
        serviceException.setRequestedType(ServiceResult.class);
        serviceException.setRequestUrl("/data/getUser");
        serviceException.addRequestParameter("userID", "45678");


        assertThat(serviceException.getFault().getCode()).isEqualTo("ILLEGAL_ARGUMENT_EXCEPTION");
        assertThat(serviceException.getFault().getMessage()).isEqualTo("The argument 'productUD' is unknown");
        assertThat(serviceException.getFault().getRequestUrl()).isEqualTo("/data/getUser");
        assertThat(serviceException.getFault().getResponseStatusCode()).isEqualTo(500);
        assertThat(serviceException.getFault().getRequestParams().get(0).getValue()).isEqualTo("45678");


        ServiceException serviceException_2 = new ServiceException("ILLEGAL_ARGUMENT_EXCEPTION", "The argument 'productUD' is unknown", 500);
        serviceException_2.setException(illegalArgumentException);
        serviceException_2.setRequestedType(ServiceResult.class);
        serviceException_2.setRequestUrl("/data/getUser");
        serviceException_2.addRequestParameter("userID", "45678");


        assertThat(serviceException_2.getFault().getCode()).isEqualTo("ILLEGAL_ARGUMENT_EXCEPTION");
        assertThat(serviceException_2.getFault().getMessage()).isEqualTo("The argument 'productUD' is unknown");
        assertThat(serviceException_2.getFault().getRequestUrl()).isEqualTo("/data/getUser");
        assertThat(serviceException_2.getFault().getResponseStatusCode()).isEqualTo(500);
        assertThat(serviceException_2.getFault().getRequestParams().get(0).getValue()).isEqualTo("45678");

    }

    @Test
    public void NameValuePairCreation() {
        NameValuePair nameValuePair_1 = new NameValuePair();
        nameValuePair_1.setName("userID");
        nameValuePair_1.setValue("123456");

        assertThat(nameValuePair_1.getValue()).isEqualTo("123456");
        assertThat(nameValuePair_1.getName()).isEqualTo("userID");

        NameValuePair nameValuePair_2 = new NameValuePair(null, null);

        assertThat(nameValuePair_2.getValue()).isEqualTo("");
        assertThat(nameValuePair_2.getName()).isEqualTo("");
    }

    @Test
    public void NameValuePairToString() {
        NameValuePair nameValuePair_1 = new NameValuePair();
        nameValuePair_1.setName("userID");
        nameValuePair_1.setValue("123456");

        assertThat(nameValuePair_1.toString()).isEqualTo("userID=123456");
    }
}
