package ch.viascom.groundwork.foxhttp.parser;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpParser {
    Serializable jsonToObject(String json, Class<Serializable> type) ;

    String objectToJson(Serializable o);
}
