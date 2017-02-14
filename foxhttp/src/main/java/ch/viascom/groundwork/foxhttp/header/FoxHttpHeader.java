package ch.viascom.groundwork.foxhttp.header;

import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * FoxHttpHeader stores headers
 *
 * @author patrick.boesch@viascom.ch
 */
@Data
public class FoxHttpHeader implements Iterable<HeaderEntry> {

    private List<HeaderEntry> headerEntries = new ArrayList<>();

    @Override
    public Iterator<HeaderEntry> iterator() {
        return headerEntries.iterator();
    }

    /**
     * Add a new header entry
     *
     * @param name  name of the header entry
     * @param value value of the header entry
     */
    public void addHeader(String name, String value) {
        headerEntries.add(new HeaderEntry(name, value));
    }

    /**
     * Add a new header entry
     *
     * @param name  name of the header entry
     * @param value value of the header entry
     */
    public void addHeader(HeaderTypes name, String value) {
        headerEntries.add(new HeaderEntry(name.toString(), value));
    }

    /**
     * Add a new map of header entries
     *
     * @param entries map of header entries
     */
    public void addHeader(Map<String, String> entries) {
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            headerEntries.add(new HeaderEntry(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Add a new array of header entries
     *
     * @param entries array of header entries
     */
    public void addHeader(List<HeaderEntry> entries) {
        headerEntries.addAll(entries);
    }

    /**
     * Get a specific header based on its name
     *
     * @param name name of the header
     * @return a specific header
     */
    public HeaderEntry getHeader(String name) {
        for (HeaderEntry headerField : getHeaderEntries()) {
            if (headerField.getName().equals(name)) {
                return headerField;
            }
        }
        return null;
    }
}
