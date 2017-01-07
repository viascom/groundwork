package ch.viascom.groundwork.foxhttp.log;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default logger for FoxHttp
 *
 * @author patrick.boesch@viascom.ch
 */
public class DefaultFoxHttpLogger implements FoxHttpLogger {

    private Logger logger = Logger.getLogger(FoxHttpClient.class.getCanonicalName());
    private boolean enabled;

    public DefaultFoxHttpLogger(boolean enabled) {
        setLoggingEnabled(enabled);
    }

    @Override
    public void setLoggingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setName(String name) {
        logger = Logger.getLogger(name);
    }

    @Override
    public void log(String message) {
        if (enabled) {
            logger.log(Level.FINE, message);
        }
    }
}
