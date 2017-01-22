package ch.viascom.groundwork.foxauth;

import ch.viascom.groundwork.foxauth.parser.FoxAuthContextParserStrategy;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidatorStrategy;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class FoxAuth {

    private FoxAuthValidatorStrategy foxAuthValidatorStrategy;
    private FoxAuthContextParserStrategy foxAuthContextParserStrategy;
}
