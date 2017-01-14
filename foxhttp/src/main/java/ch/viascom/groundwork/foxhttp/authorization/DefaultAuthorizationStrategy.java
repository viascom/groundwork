package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.util.RegexUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URLConnection;
import java.util.*;

/**
 * Default AuthorizationStrategy for FoxHttp
 * <p>
 * Stores FoxHttpAuthorization with a FoxHttpAuthorizationScope as key.
 *
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class DefaultAuthorizationStrategy implements FoxHttpAuthorizationStrategy {

    /**
     * AuthorizationStrategy store
     */
    @Getter
    @Setter
    private HashMap<String, ArrayList<FoxHttpAuthorization>> foxHttpAuthorizations = new HashMap<>();

    /**
     * Returns a list of matching FoxHttpAuthorizations based on the given FoxHttpAuthorizationScope
     *
     * @param connection                connection of the request
     * @param foxHttpAuthorizationScope looking for scope
     * @return
     */
    @Override
    public List<FoxHttpAuthorization> getAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope) {
        ArrayList<FoxHttpAuthorization> foxHttpAuthorizationList = new ArrayList<>();

        for (Map.Entry<String, ArrayList<FoxHttpAuthorization>> entry : foxHttpAuthorizations.entrySet()) {
            if (RegexUtil.doesURLMatch(foxHttpAuthorizationScope.toString(), entry.getKey())) {
                foxHttpAuthorizationList.addAll(entry.getValue());
            }
        }

        if (foxHttpAuthorizations.containsKey(FoxHttpAuthorizationScope.ANY.toString()) && (foxHttpAuthorizationList.isEmpty())) {
            foxHttpAuthorizationList.addAll(foxHttpAuthorizations.get(FoxHttpAuthorizationScope.ANY.toString()));
        }

        return foxHttpAuthorizationList;
    }

    /**
     * Add a new FoxHttpAuthorization to the AuthorizationStrategy
     *
     * @param foxHttpAuthorizationScope scope in which the authorization is used
     * @param foxHttpAuthorization      authorization itself
     */
    @Override
    public void addAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpAuthorization foxHttpAuthorization) {
        if (foxHttpAuthorizations.containsKey(foxHttpAuthorizationScope.toString())) {
            foxHttpAuthorizations.get(foxHttpAuthorizationScope.toString()).add(foxHttpAuthorization);
        } else {
            foxHttpAuthorizations.put(foxHttpAuthorizationScope.toString(), new ArrayList<>(Collections.singletonList(foxHttpAuthorization)));
        }

    }

    /**
     * Remove a defined FoxHttpAuthorization from the AuthorizationStrategy
     *
     * @param foxHttpAuthorizationScope scope in which the authorization is used
     * @param foxHttpAuthorization      object of the same authorization
     */
    @Override
    public void removeAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpAuthorization foxHttpAuthorization) {
        ArrayList<FoxHttpAuthorization> authorizations = foxHttpAuthorizations.get(foxHttpAuthorizationScope.toString());
        ArrayList<FoxHttpAuthorization> cleandAuthorizations = new ArrayList<>();
        for (FoxHttpAuthorization authorization : authorizations) {
            if (authorization.getClass() != foxHttpAuthorization.getClass()) {
                cleandAuthorizations.add(authorization);
            }
        }
        foxHttpAuthorizations.put(foxHttpAuthorizationScope.toString(), cleandAuthorizations);
    }
}
