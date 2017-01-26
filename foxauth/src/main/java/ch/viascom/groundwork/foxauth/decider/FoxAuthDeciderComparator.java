package ch.viascom.groundwork.foxauth.decider;

import java.util.Comparator;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxAuthDeciderComparator implements Comparator<FoxAuthDecider> {
    @Override
    public int compare(FoxAuthDecider o1, FoxAuthDecider o2) {
        return o1.getWeight() - o2.getWeight();
    }
}
