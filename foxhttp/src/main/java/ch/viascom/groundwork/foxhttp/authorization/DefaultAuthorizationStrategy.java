package ch.viascom.groundwork.foxhttp.authorization;

import lombok.Getter;
import lombok.Setter;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultAuthorizationStrategy implements FoxHttpAuthorizationStrategy {

    @Getter
    @Setter
    private HashMap<String, FoxHttpAuthorization> foxHttpAuthorizations = new HashMap<>();

    @Override
    public ArrayList<FoxHttpAuthorization> getAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope) {
        ArrayList<FoxHttpAuthorization> foxHttpAuthorizationList = new ArrayList<>();

        if (foxHttpAuthorizations.containsKey(foxHttpAuthorizationScope.toString())) {
            foxHttpAuthorizationList.add(foxHttpAuthorizations.get(foxHttpAuthorizationScope.toString()));
        } else if (foxHttpAuthorizations.containsKey(FoxHttpAuthorizationScope.ANY.toString())) {
            foxHttpAuthorizationList.add(foxHttpAuthorizations.get(FoxHttpAuthorizationScope.ANY.toString()));
        }

        return foxHttpAuthorizationList;
    }

    @Override
    public void addAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpAuthorization foxHttpAuthorization) {
        foxHttpAuthorizations.put(foxHttpAuthorizationScope.toString(), foxHttpAuthorization);
    }

    @Override
    public void removeAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, Class<FoxHttpAuthorization> foxHttpAuthorizationClass) {
        foxHttpAuthorizations.remove(foxHttpAuthorizationScope.toString());
    }
}
