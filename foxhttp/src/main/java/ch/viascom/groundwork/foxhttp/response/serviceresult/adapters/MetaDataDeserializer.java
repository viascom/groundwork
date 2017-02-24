package ch.viascom.groundwork.foxhttp.response.serviceresult.adapters;

import ch.viascom.groundwork.serviceresult.util.Metadata;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author patrick.boesch@viascom.ch
 */
public class MetaDataDeserializer implements JsonDeserializer<Metadata> {
    @Override
    public Metadata deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        String metaDataType = jsonElement.getAsJsonObject().getAsJsonPrimitive("type").getAsString();
        try {
            Serializable o = jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("content"), Class.forName(metaDataType));
            return new Metadata<>(metaDataType, o);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Could not find class " + metaDataType, e);
        }

    }
}
