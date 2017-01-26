package ch.viascom.groundwork.foxauth.decider;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@NoArgsConstructor
public class FoxAuthHttpServletRequestWrapper {

    private HttpServletRequest httpServletRequest;
    private ByteArrayOutputStream body = new ByteArrayOutputStream();
    private String method;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> query = new HashMap<>();


    public FoxAuthHttpServletRequestWrapper(HttpServletRequest httpServletRequest) throws IOException {
        this.httpServletRequest = httpServletRequest;

        processBody(httpServletRequest.getInputStream());
        processHeader(httpServletRequest.getHeaderNames());
        processQuery(httpServletRequest.getParameterNames());
        method = httpServletRequest.getMethod();
    }

    public void processHeader(Enumeration<String> headerNames) {
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, httpServletRequest.getHeader(name));
        }
    }

    public void processQuery(Enumeration<String> queryNames) {
        while (queryNames.hasMoreElements()) {
            String name = queryNames.nextElement();
            query.put(name, httpServletRequest.getParameter(name));
        }
    }

    public void processBody(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                body.write(buffer, 0, len);
            }
            body.flush();
        }
    }
}
