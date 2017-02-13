package ch.viascom.groundwork.serviceresult;

import ch.viascom.groundwork.serviceresult.util.Metadata;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;


/**
 * @author patrick.boesch@viascom.ch
 */
public class ServiceResultTest {

    @Test
    public void ServiceResultWithNoContent() {
        ServiceResult<String> result = new ServiceResult<>(String.class);
        assertThat(result.getContent()).isEqualTo(null);
    }

    @Test
    public void ServiceResultWithContent() {
        ServiceResult<String> result = new ServiceResult<>(String.class);
        result.setContent("Content");
        result.setDestination("127.0.0.1");
        assertThat(result.getContent()).isEqualTo("Content");
        assertThat(result.getDestination()).isEqualTo("127.0.0.1");
    }

    @Test
    public void ServiceResultConstructor() {
        ServiceResult<String> result = new ServiceResult<>(String.class, "Content");
        assertThat(result.getContent()).isEqualTo("Content");

        ServiceResult<String> result2 = new ServiceResult<>(String.class, "Content", ServiceResultStatus.successful);
        assertThat(result2.getContent()).isEqualTo("Content");
        assertThat(result2.getStatus()).isEqualTo(ServiceResultStatus.successful);

        ServiceResult<Integer> result3 = new ServiceResult<>(Integer.class, 12, ServiceResultStatus.failed);
        assertThat(result3.getContent()).isEqualTo(12);
        assertThat(result3.getStatus()).isEqualTo(ServiceResultStatus.failed);
    }

    @Test
    public void ServiceResultMetadata() {
        ServiceResult<String> result = new ServiceResult<>(String.class, "Content", ServiceResultStatus.successful);
        result.addMetadata("ch.viascom.groundwork.test", new Metadata(String.class, "Metadata"));
        assertThat(result.getContent()).isEqualTo("Content");
        assertThat(result.getStatus()).isEqualTo(ServiceResultStatus.successful);
        assertThat(result.getType()).isEqualTo(String.class.getCanonicalName());
        assertThat(result.getMetadata().get("ch.viascom.groundwork.test").getContent()).isEqualTo("Metadata");
        assertThat(result.getMetadata().get("ch.viascom.groundwork.test").getType()).isEqualTo(String.class.getCanonicalName());

        ServiceResult<String> result2 = new ServiceResult<>(String.class, "Content", ServiceResultStatus.successful);

        Metadata<Integer> integerMetadata = new Metadata<>(Integer.class);
        integerMetadata.setContent(12);

        result2.addMetadata("ch.viascom.groundwork.test", integerMetadata);
        assertThat(result2.getContent()).isEqualTo("Content");
        assertThat(result2.getStatus()).isEqualTo(ServiceResultStatus.successful);
        assertThat(result2.getType()).isEqualTo(String.class.getCanonicalName());
        assertThat(result2.getMetadata().get("ch.viascom.groundwork.test").getContent()).isEqualTo(12);
        assertThat(result2.getMetadata().get("ch.viascom.groundwork.test").getType()).isEqualTo(Integer.class.getCanonicalName());
    }
}
