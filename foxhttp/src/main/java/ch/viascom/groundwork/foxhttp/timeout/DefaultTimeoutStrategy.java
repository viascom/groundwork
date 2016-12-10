package ch.viascom.groundwork.foxhttp.timeout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultTimeoutStrategy implements FoxHttpTimeoutStrategy {
    private int connectionTimeout = 0;
    private int readTimeout = 0;
}
