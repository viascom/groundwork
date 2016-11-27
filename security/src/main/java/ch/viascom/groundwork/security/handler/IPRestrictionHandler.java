package ch.viascom.groundwork.security.handler;


import ch.viascom.groundwork.security.models.IPModel;

import java.util.List;

public class IPRestrictionHandler {

    /**
     * Checks if the given IP is listed in the list of authorized IPs.<br>
     * It returns false if the given IP is Empty or NULL.
     *
     * @param ip An IP given as a String
     * @return Returns true if the IP is permitted.
     */
    public boolean isAuthorized(String ip, List<IPModel> authorizedIPs) {
        if (ip == null || ip.isEmpty())
            return false;

        ip = ip.trim();

        for (IPModel ipModel : authorizedIPs) {
            if (ip.equals(ipModel.getIP()))
                return true;
        }

        return false;
    }

}
