package ch.viascom.groundwork.foxhttp.log;

import lombok.AllArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * System.Out logger for FoxHttp
 *
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class SystemOutFoxHttpLogger implements FoxHttpLogger {

    private boolean enabled = false;
    private String loggerName = "";

    @Override
    public void setLoggingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setName(String name) {
        loggerName = name;
    }

    @Override
    public void log(String message) {
        log(message, loggerName);
    }

    public void log(String message, String name) {
        if (enabled) {
            System.out.println("[" + getTime() + "] - " + name + ": " + message);
        }
    }

    private String getTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date();
        return df.format(date);
    }
}
