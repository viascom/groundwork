package ch.viascom.groundwork.foxhttp.placeholder;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultPlaceholderStrategy implements FoxHttpPlaceholderStrategy {

    /**
     * Start of the placeholder
     */
    @Getter
    @Setter
    private String placeholderEscapeCharStart = "{";

    /**
     * End of the placeholder
     */
    @Getter
    @Setter
    private String placeholderEscapeCharEnd = "}";

    /**
     * Regex to check if placeholders are used
     */
    @Getter
    @Setter
    private String placeholderMatchRegex = "[\\{][ -z|]*[\\}]";

    @Getter
    @Setter
    private Map<String, String> placeholderMap = new HashMap<>();

    /**
     * Add a placeholder to the strategy
     *
     * @param placeholder name of the placholder
     * @param value       value of the placeholder
     */
    @Override
    public void addPlaceholder(String placeholder, String value) {
        placeholderMap.put(placeholder, value);
    }

}
