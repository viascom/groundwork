package ch.viascom.groundwork.foxauth;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.inject.Produces;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxAuthProvider {

    @Getter
    @Setter
    private static FoxAuth foxAuth;

    public FoxAuthProvider(FoxAuth foxAuth) {
        FoxAuthProvider.foxAuth = foxAuth;
    }

    public FoxAuthProvider() {
        if (FoxAuthProvider.foxAuth == null) {
            FoxAuthProvider.foxAuth = new FoxAuth();
        }
    }

    @Produces
    public FoxAuth produceFoxAuth() {
        System.out.println("-> Call method FoxAuthProvider:produceFoxAuth");
        return foxAuth;
    }
}
