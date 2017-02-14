package ch.viascom.groundwork.foxhttp.component;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpComponent {
    void initiation(FoxHttpClient foxHttpClient) throws FoxHttpException;
}
