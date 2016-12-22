package ch.viascom.groundwork.restclient.android.model;

import ch.viascom.groundwork.restclient.response.generic.Response;
import ch.viascom.groundwork.restclient.response.generic.ResponseHeader;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class QueryParamReturn implements Response {

    private ResponseHeader responseHeader;

    @SerializedName("Content-Length")
    private String contentLength;

    @SerializedName("Content-Type")
    private String contentType;

    @SerializedName("Viascom-Framework")
    private String viascomFramework;
}
