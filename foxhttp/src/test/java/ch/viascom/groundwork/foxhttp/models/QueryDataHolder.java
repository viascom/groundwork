package ch.viascom.groundwork.foxhttp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
public class QueryDataHolder {
    private String name;
    private int index;
    private String key;
}
