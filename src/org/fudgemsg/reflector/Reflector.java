package org.fudgemsg.reflector;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static org.reflections.scanners.Scanners.MethodsParameter;

public class Reflector extends Reflections {

    public Reflector()
    {
        super();
    }

    public Reflector(Configuration configuration)
    {
        super(configuration);
    }

    public Reflector(Store store)
    {
        super(store);
    }

    public Reflector(String prefix, Scanner... scanners)
    {

        super(prefix, scanners);
    }


    public Reflector(Object... params) {
        super(params);
    }

    protected Map<String, Map<String, Set<String>>> scan()
    {
        return super.scan();
    }

    public Set<Method> getMethodsWithAnyParamAnnotated(Class<? extends Annotation> annotationClass)
    {
        return get(MethodsParameter.with(annotationClass).as(Method.class, loaders()));
    }

    ClassLoader[] loaders() { return super.configuration.getClassLoaders(); }

    public Store getStore2()
    {
        return new Store(this, super.store);
    }
}
