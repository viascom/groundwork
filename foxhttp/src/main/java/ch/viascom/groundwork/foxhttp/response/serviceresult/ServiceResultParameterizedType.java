package ch.viascom.groundwork.foxhttp.response.serviceresult;

import ch.viascom.groundwork.serviceresult.ServiceResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ServiceResultParameterizedType implements ParameterizedType {

    private Type type;

    public ServiceResultParameterizedType(Type type) {
        this.type = type;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[] {type};
    }

    @Override
    public Type getRawType() {
        return ServiceResult.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
