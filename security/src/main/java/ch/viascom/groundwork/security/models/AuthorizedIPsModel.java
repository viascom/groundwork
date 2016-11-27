package ch.viascom.groundwork.security.models;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class AuthorizedIPsModel implements Serializable  {

        private final List<IPModel> authorizedIPs = new ArrayList<IPModel>();

}