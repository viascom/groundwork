package ch.viascom.groundwork.foxhttp.interceptor;

import java.util.Comparator;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpInterceptorComparator implements Comparator<FoxHttpInterceptor> {
    @Override
    public int compare(FoxHttpInterceptor o1, FoxHttpInterceptor o2) {
        return o1.getWeight() - o2.getWeight();
    }
}
