package ch.viascom.groundwork.foxhttp.header;

import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class FoxHttpHeader implements Iterable<HeaderField> {

    private List<HeaderField> headerFields = new ArrayList<>();

    @Override
    public Iterator<HeaderField> iterator() {
        return headerFields.iterator();
    }

    public void addHeader(String name, String value) {
        headerFields.add(new HeaderField(name, value));
    }

    public void addHeader(HeaderTypes name, String value) {
        headerFields.add(new HeaderField(name.toString(), value));
    }

    public HeaderField getHeader(String name) {
        for (HeaderField headerField : getHeaderFields()) {
            if (headerField.getName().equals(name)) {
                return headerField;
            }
        }
        return null;
    }
}
