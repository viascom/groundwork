package ch.viascom.groundwork.foxhttp.timeout;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@ToString
public class DefaultTimeoutStrategy implements FoxHttpTimeoutStrategy {
    private int connectionTimeout = 0;
    private int readTimeout = 0;
}
