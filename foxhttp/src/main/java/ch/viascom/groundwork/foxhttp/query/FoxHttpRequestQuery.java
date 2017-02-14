package ch.viascom.groundwork.foxhttp.query;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.util.QueryBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * FoxHttpRequestQuery stores query entries for a request
 *
 * @author patrick.boesch@viascom.ch
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FoxHttpRequestQuery {
    private HashMap<String, String> queryMap = new HashMap<>();

    /**
     * Add a new query entry
     *
     * @param name  name of the query entry
     * @param value value of the query entry
     */
    public void addQueryEntry(String name, String value) {
        queryMap.put(name, value);
    }

    /**
     * Add a map of new query entries
     *
     * @param queryMap map of query entries
     */
    public void addQueryMap(HashMap<String, String> queryMap) {
        this.queryMap.putAll(queryMap);
    }

    /**
     * Remove a query entry
     *
     * @param name name of the query entry
     */
    public void removeQueryEntry(String name) {
        queryMap.remove(name);
    }

    /**
     * Check if there are query entries stored
     *
     * @return true if more than 0 entries stored
     */
    public boolean hasQueryEntries() {
        return queryMap.size() > 0;
    }

    /**
     * Get all stored queries as string
     *
     * @return string with all query entries
     * @throws FoxHttpRequestException can throw an exception if the "UTF-8" encoding is not found
     */
    public String getQueryString() throws FoxHttpRequestException {
        return "?" + QueryBuilder.buildQuery(queryMap);
    }

    /**
     * Parse an object as query map
     *
     * @param params list of attribute names which will be included
     * @param o      object with the attributes
     * @throws FoxHttpRequestException can throw an exception if a field does not exist
     */
    public void parseObjectAsQueryMap(List<String> params, Object o) throws FoxHttpRequestException {
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
