package ch.viascom.groundwork.foxhttp.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author patrick.boesch@viascom.ch
 */
class FoxHttpAnnotationUtil {
    static boolean hasMethodAnnotation(Class<? extends Annotation> annotationClass, Method method) {
        return method.getAnnotation(annotationClass) != null;
    }

    static boolean hasTypeAnnotation(Class<? extends Annotation> annotationClass, Method method) {
        return method.getDeclaringClass().getAnnotation(annotationClass) != null;
    }

    static boolean hasParameterAnnotation(Class<? extends Annotation> annotationClass, Method method) {
        return getParameterAnnotation(annotationClass, method) > 0;
    }

    static int getParameterAnnotation(Class<? extends Annotation> annotationClass, Method method) {
        int count = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    count++;
                }
            }
        }
        return count;
    }
}
