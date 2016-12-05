package ch.viascom.groundwork.restclient.http;

import ch.viascom.groundwork.restclient.http.filter.PerformancePathRequestFilter;
import ch.viascom.groundwork.restclient.http.request.simple.SimpleGetRequest;
import ch.viascom.groundwork.restclient.http.util.Stopwatch;
import ch.viascom.groundwork.restclient.response.NoContentResponse;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FilterPerformanceTest {
    @Ignore("Only for performance testing")
    @Test
    public void executionTimeTest() throws Exception {

        String url = "http://httpbin.org";
        String path = "/links/";

        double with = 0;
        double without = 0;
        int counter = 0;

        for (int i = 0; i < 100; i++) {
            //Without Chain
            SimpleGetRequest<NoContentResponse> request2 = new SimpleGetRequest<>(url, NoContentResponse.class);
            request2.setMediaType("application/json");
            request2.setPath(path + i);
            Stopwatch stopwatch2 = Stopwatch.startNew();
            request2.execute();
            stopwatch2.stop();

            //With Chain
            SimpleGetRequest<NoContentResponse> request = new SimpleGetRequest<>(url, NoContentResponse.class);
            request.setMediaType("application/json");
            request.setPath("" + i);
            request.register(new PerformancePathRequestFilter());
            Stopwatch stopwatch = Stopwatch.startNew();
            request.execute();
            stopwatch.stop();

            with += stopwatch.getElapsedMilliseconds();
            without += stopwatch2.getElapsedMilliseconds();
            counter++;
        }

        double averageWith = with / counter;
        double averageWithout = without / counter;

        System.out.println("Average with Chain over " + counter + " Requests :    " + averageWith);
        System.out.println("Average without Chain over " + counter + " Requests : " + averageWithout);
        System.out.println("Without Chain is "+ (averageWith-averageWithout) + " ms faster");

    }
}
