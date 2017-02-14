package ch.viascom.groundwork.foxhttp.interfaces;

import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.annotation.types.*;
import ch.viascom.groundwork.foxhttp.body.request.RequestStringBody;
import ch.viascom.groundwork.foxhttp.header.HeaderEntry;
import ch.viascom.groundwork.foxhttp.models.GetResponse;
import ch.viascom.groundwork.foxhttp.models.PostResponse;
import ch.viascom.groundwork.foxhttp.util.NamedInputStream;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Path("{host}")
public interface FoxHttpInterfaceTest {

    @GET("get")
    GetResponse get(@Query("key") String key);

    @GET("get")
    @Header(name = "foo", value = "bar")
    GetResponse bigGet(@QueryMap HashMap<String, String> queryMap, @HeaderField("Product") String product);

    @GET("{path}")
    @Header(name = "foo", value = "bar")
    FoxHttpRequest getRequest(@Path("path") String path);

    @POST("post")
    PostResponse postBody(@Body RequestStringBody stringBody, @HeaderFieldMap ArrayList<HeaderEntry> headerFields);

    @POST("post")
    @FormUrlEncodedBody
    PostResponse postForm(@Field("username") String username, @Field("password") String password, @HeaderFieldMap HashMap<String, String> headerFields);

    @POST("post")
    @FormUrlEncodedBody
    PostResponse postFormMap(@FieldMap HashMap<String, String> fromData);

    @POST("post")
    @MultipartBody
    FoxHttpResponse postMulti(@Part("test") String text, @Part("stream") NamedInputStream namedInputStream);

    @POST("post")
    @MultipartBody
    FoxHttpResponse postMultiMap(@PartMap(isStreamMap = true) HashMap<String, NamedInputStream> namedInputStreamMap);
}
