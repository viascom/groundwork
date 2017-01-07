package ch.viascom.groundwork.serviceresult.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class Metadata<T extends Serializable> implements Serializable {
    private String type;
    private T content;

    public Metadata(String type) {
        this.setType(type);
    }

    public Metadata(Class<T> type) {
        this.setType(type.getCanonicalName());
    }

    public Metadata(String type, T metadata) {
        this.setType(type);
        this.setContent(metadata);
    }

    public Metadata(Class<T> type, T metadata) {
        this.setType(type.getCanonicalName());
        this.setContent(metadata);
    }
}
