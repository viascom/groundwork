package ch.viascom.groundwork.foxauth;

import ch.viascom.groundwork.foxauth.decider.FoxAuthDecider;
import ch.viascom.groundwork.foxauth.response.error.FoxAuthErrorResponse;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoxAuthValidation {

    private boolean status;
    private List<String> scopes;
    private String state;
    private String clientId;
    private String redirectURI;

    private FoxAuthDecider foxAuthDecider;
    private FoxAuthValidator foxAuthValidator;

    private String errorCode;
    private String errorMessage;
    private String errorUrl;

    public void setFoxAuthErrorResponse(FoxAuthErrorResponse foxAuthErrorResponse) {
        this.errorCode = foxAuthErrorResponse.getError();
        this.errorMessage = foxAuthErrorResponse.getError_description();
        this.errorUrl = foxAuthErrorResponse.getError_uri();
    }
}
