package ch.viascom.groundwork.foxhttp.query;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.util.QueryBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FoxHttpRequestQuery {
    private HashMap<String, String> queryMap = new HashMap<>();

    public void addQueryEntry(String name, String value) {
        queryMap.put(name, value);
    }

    public void removeQueryEntry(String name) {
        queryMap.remove(name);
    }

    public boolean hasQueryEntries() {
        return (queryMap.size() > 0);
    }

    public String getQueryString() throws FoxHttpRequestException {
        return "?" + QueryBuilder.buildQuery(queryMap);
    }

    public void parseObjectAsQueryMap(ArrayList<String> params, Object o) throws FoxHttpRequestException {
        try {
            Class clazz = o.getClass();
            HashMap<String, String> paramMap = new HashMap<>();
            for (String param : params) {
                Field field = clazz.getDeclaredField(param);
                field.setAccessible(true);
                String paramName = field.getName();
                String value = String.valueOf(field.get(o));
                if (field.get(o) != null && !value.isEmpty()) {
                    paramMap.put(paramName, value);
                }
            }
            queryMap = paramMap;
        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
        }
    }

}
