package ch.viascom.groundwork.foxhttp.placeholder;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
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


    public String processPlaceholders(String processedURL, FoxHttpClient foxHttpClient) {
        for (Map.Entry<String, String> entry : this.getPlaceholderMap().entrySet()) {
            foxHttpClient.getFoxHttpLogger().log("-> " + this.getPlaceholderEscapeCharStart() + entry.getKey() + this.getPlaceholderEscapeCharEnd() + " -> " + entry.getValue());
            processedURL = processedURL.replace(this.getPlaceholderEscapeCharStart() + entry.getKey() + this.getPlaceholderEscapeCharEnd(), entry.getValue());
        }
        return processedURL;
    }
}
