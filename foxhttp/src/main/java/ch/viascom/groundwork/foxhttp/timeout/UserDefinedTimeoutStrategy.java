package ch.viascom.groundwork.foxhttp.timeout;

import lombok.*;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDefinedTimeoutStrategy implements FoxHttpTimeoutStrategy {
    private int connectionTimeout = 0;
    private int readTimeout = 0;
}
