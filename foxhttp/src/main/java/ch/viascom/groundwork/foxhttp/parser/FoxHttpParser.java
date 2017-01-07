package ch.viascom.groundwork.foxhttp.parser;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpParser {
    Serializable serializedToObject(String input, Class<Serializable> type) ;

    String objectToSerialized(Serializable o);
}
