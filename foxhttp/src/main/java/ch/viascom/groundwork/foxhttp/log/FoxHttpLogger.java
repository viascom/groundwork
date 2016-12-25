package ch.viascom.groundwork.foxhttp.log;

/**
 * FoxHttpLogger interface
 *
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpLogger {
    void setLoggingEnabled(boolean enabled);
    void setName(String name);
    void log(String message);
}
