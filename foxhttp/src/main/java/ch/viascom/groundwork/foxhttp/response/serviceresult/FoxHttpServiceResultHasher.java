package ch.viascom.groundwork.foxhttp.response.serviceresult;

import ch.viascom.groundwork.foxhttp.response.FoxHttpResultHasher;
import ch.viascom.groundwork.serviceresult.ServiceResult;

/**
 * @author patrick.boesch@viascom.ch
 */
@FunctionalInterface
public interface FoxHttpServiceResultHasher extends FoxHttpResultHasher {
    String hash(ServiceResult result, String rawBody);
}
