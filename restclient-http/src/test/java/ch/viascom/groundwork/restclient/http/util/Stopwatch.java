package ch.viascom.groundwork.restclient.http.util;

/**
 * @author patrick.boesch@viascom.ch
 */
public class Stopwatch {
    public final double TICKS_PER_MILLISECOND = 1000000d;
    public final double TICKS_PER_SECOND = 1000000000d;

    private long elapsed;
    private boolean isRunning;
    private long startTimeStamp;


    /**
     * Initializes a new instance of the Stopwatch class.
     * <p>
     * Time measurement is turned off.
     * To create and start a watch at the same time, use Stopwatch.startNew().
     */
    public Stopwatch() {
        reset();
    }

    /**
     * Gets the current number of ticks in the timer mechanism.
     */
    public static long getTimestamp() {
        return System.nanoTime();
    }

    /**
     * Initializes a new Stopwatch instance, sets the elapsed time property to zero, and starts measuring elapsed time.
     */
    public static Stopwatch startNew() {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        return stopwatch;
    }

    /**
     * Elapsed time in seconds.
     */
    public double getElapsedSeconds() {
        return calcElapsedTicks() / TICKS_PER_SECOND;
    }

    /**
     * Elapsed time in milliseconds.
     */
    public double getElapsedMilliseconds() {
        return calcElapsedTicks() / TICKS_PER_MILLISECOND;
    }

    /**
     * Elapsed ticks since start.
     */
    public long getElapsedTicks() {
        return calcElapsedTicks();
    }

    /**
     * Indicates if the Stopwatch is running and measuring time.
     */
    public boolean getIsRunning() {
        return isRunning;
    }

    /**
     * Stops time interval measurement and resets the elapsed time to zero.
     */
    public void reset() {
        elapsed = 0L;
        isRunning = false;
        startTimeStamp = 0L;
    }

    /**
     * Stops time interval measurement, resets the elapsed time to zero, and starts measuring elapsed time.
     */
    public void restart() {
        elapsed = 0L;
        startTimeStamp = getTimestamp();
        isRunning = true;
    }

    /**
     * Starts, or resumes, measuring elapsed time for an interval.
     */
    public void start() {
        if (!isRunning) {
            startTimeStamp = getTimestamp();
            isRunning = true;
        }
    }

    /**
     * Stops measuring elapsed time for an interval.
     */
    public void stop() {
        if (isRunning) {
            elapsed += getTimestamp() - startTimeStamp;
            isRunning = false;

            if (elapsed < 0L)
                elapsed = 0L;
        }
    }

    private long calcElapsedTicks() {
        long rawElapsed = elapsed;

        if (isRunning)
            rawElapsed += getTimestamp() - startTimeStamp;

        return rawElapsed;
    }
}

