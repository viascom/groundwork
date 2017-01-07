package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.models.User;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.parser.XStreamParser;
import org.junit.Test;

import java.io.Serializable;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpParserTest {

    @Test
    public void gsonTest() throws Exception {
        User user = new User();

        String json = new GsonParser().objectToSerialized(user);
        User deUser = (User) new GsonParser().serializedToObject(json, (Class<Serializable>) Class.forName("ch.viascom.groundwork.foxhttp.models.User"));

        assertThat(user).isEqualTo(deUser);
    }

    @Test
    public void xstreamTest() throws Exception {
        User user = new User();

        String json = new XStreamParser().objectToSerialized(user);
        User deUser = (User) new XStreamParser().serializedToObject(json, (Class<Serializable>) Class.forName("ch.viascom.groundwork.foxhttp.models.User"));

        assertThat(user).isEqualTo(deUser);
    }
}
