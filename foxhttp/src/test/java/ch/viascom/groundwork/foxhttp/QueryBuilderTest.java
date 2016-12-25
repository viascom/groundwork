package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.util.QueryBuilder;
import org.junit.Test;

import java.util.HashMap;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class QueryBuilderTest {

    @Test
    public void buildQueryTest_Empty() throws Exception {
        String expectedQuery = "";
        String outputQuery = "";
        HashMap<String, String> inputEntries = new HashMap<>();

        outputQuery = QueryBuilder.buildQuery(inputEntries);

        assertThat(outputQuery).isEqualTo(expectedQuery);
    }

    @Test
    public void buildQueryTest() throws Exception {
        String expectedQuery = "name2=value2&name1=value1";
        String outputQuery = "";
        HashMap<String, String> inputEntries = new HashMap<>();

        inputEntries.put("name1","value1");
        inputEntries.put("name2","value2");

        outputQuery = QueryBuilder.buildQuery(inputEntries);

        assertThat(outputQuery).isEqualTo(expectedQuery);
    }
}
