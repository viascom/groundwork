package ch.viascom.groundwork.foxhttp.response;

import ch.viascom.groundwork.serviceresult.ServiceResult;
import ch.viascom.groundwork.serviceresult.util.ObjectHasher;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultServiceResultHasher implements FoxHttpServiceResultHasher {
    @Override
    public String hash(ServiceResult result, String rawBody) {
        return ObjectHasher.hash(result.getContent());
    }
}
