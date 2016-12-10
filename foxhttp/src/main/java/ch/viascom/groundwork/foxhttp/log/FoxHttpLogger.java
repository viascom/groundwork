package ch.viascom.groundwork.foxhttp.log;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpLogger {
    void setLoggingEnabled(boolean enabled);
    void setName(String name);
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
}
