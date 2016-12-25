package ch.viascom.groundwork.foxhttp.response.serviceresult;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.serviceresult.exception.ServiceFault;
import lombok.Getter;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpServiceResultException extends FoxHttpResponseException {

    @Getter
    private ServiceFault serviceFault;

    public FoxHttpServiceResultException(ServiceFault serviceFault) {
        super(serviceFault.getMessage());
        this.serviceFault = serviceFault;
    }

    public FoxHttpServiceResultException(Throwable cause) {
        super(cause);
    }

    public FoxHttpServiceResultException(String message) {
        super(message);
    }

    public FoxHttpServiceResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
