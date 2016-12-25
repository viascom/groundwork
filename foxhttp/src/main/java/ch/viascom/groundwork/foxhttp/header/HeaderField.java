package ch.viascom.groundwork.foxhttp.header;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@AllArgsConstructor
@ToString
public class HeaderField {
    private String name;
    private String value;
}
