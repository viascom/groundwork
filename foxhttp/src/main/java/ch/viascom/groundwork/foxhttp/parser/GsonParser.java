package ch.viascom.groundwork.foxhttp.parser;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
public class GsonParser implements FoxHttpParser {
    @Override
    public Serializable jsonToObject(String json, Class<Serializable> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    @Override
    public String objectToJson(Serializable o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }
}
