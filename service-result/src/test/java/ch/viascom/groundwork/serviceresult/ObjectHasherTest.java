package ch.viascom.groundwork.serviceresult;

import ch.viascom.groundwork.serviceresult.util.ObjectHasher;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ObjectHasherTest {

    @Test
    public void hashTest() {
        ServiceResult<String> result = new ServiceResult<>(String.class);
        result.setContent("Test");

        String hash = ObjectHasher.hash(result.getContent());
        assertThat(hash).isEqualTo("ec83b74cf71e43de87afa3b13997fb2a2391aa");

        hash = ObjectHasher.hash(null);
        assertThat(hash).isEqualTo("");
    }

}
