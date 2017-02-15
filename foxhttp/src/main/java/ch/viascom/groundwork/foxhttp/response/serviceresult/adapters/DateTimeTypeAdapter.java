package ch.viascom.groundwork.foxhttp.response.serviceresult.adapters;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

/**
 * @author patrick.boesch@viascom.ch
 */
public final class DateTimeTypeAdapter implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {

    static final org.joda.time.format.DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

    @Override
    public DateTime deserialize(final JsonElement je, final Type type, final JsonDeserializationContext jdc) throws JsonParseException {
        return je.getAsString().length() == 0 ? null : DATE_TIME_FORMATTER.parseDateTime(je.getAsString());
    }

    @Override
    public JsonElement serialize(final DateTime src, final Type typeOfSrc, final JsonSerializationContext context) {
        return new JsonPrimitive(src == null ? StringUtils.EMPTY : DATE_TIME_FORMATTER.print(src));
    }
}
