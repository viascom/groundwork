package ch.viascom.groundwork.foxhttp.header;

import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * FoxHttpHeader stores headers
 *
 * @author patrick.boesch@viascom.ch
 */
@Data
public class FoxHttpHeader implements Iterable<HeaderField> {

    private List<HeaderField> headerFields = new ArrayList<>();

    @Override
    public Iterator<HeaderField> iterator() {
        return headerFields.iterator();
    }

    /**
     * Add a new header entry
     *
     * @param name  name of the header entry
     * @param value value of the header entry
     */
    public void addHeader(String name, String value) {
        headerFields.add(new HeaderField(name, value));
    }

    /**
     * Add a new header entry
     *
     * @param name  name of the header entry
     * @param value value of the header entry
     */
    public void addHeader(HeaderTypes name, String value) {
        headerFields.add(new HeaderField(name.toString(), value));
    }

    /**
     * Get a specific header based on its name
     *
     * @param name name of the header
     * @return a specific header
     */
    public HeaderField getHeader(String name) {
        for (HeaderField headerField : getHeaderFields()) {
            if (headerField.getName().equals(name)) {
                return headerField;
            }
        }
        return null;
    }
}
