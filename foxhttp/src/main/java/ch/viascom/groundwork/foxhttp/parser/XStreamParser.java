package ch.viascom.groundwork.foxhttp.parser;

import com.thoughtworks.xstream.XStream;

import java.io.Serializable;


/**
 * @author patrick.boesch@viascom.ch
 */
public class XStreamParser implements FoxHttpParser {

    @Override
    public Serializable serializedToObject(String input, Class<Serializable> type) {
        XStream xStream = new XStream();
        return (Serializable) xStream.fromXML(input);
    }

    @Override
    public String objectToSerialized(Serializable o) {
        XStream xStream = new XStream();
        return xStream.toXML(o);
    }
}
