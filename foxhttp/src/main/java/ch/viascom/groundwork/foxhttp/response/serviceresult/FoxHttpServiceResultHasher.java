package ch.viascom.groundwork.foxhttp.response.serviceresult;

import ch.viascom.groundwork.serviceresult.ServiceResult;

/**
 * @author patrick.boesch@viascom.ch
 */
@FunctionalInterface
public interface FoxHttpServiceResultHasher {
    String hash(ServiceResult result, String rawBody);
}
