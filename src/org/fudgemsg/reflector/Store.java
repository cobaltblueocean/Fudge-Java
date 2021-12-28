package org.fudgemsg.reflector;

import org.reflections.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Store extends org.reflections.Store {
    private Configuration _configuration;
    private Reflector _baseReflector;

    public Store(Reflector base)
    {
        _baseReflector = base;
    }

    public Store(Reflector base, Map<String, Map<String, Set<String>>> storeMap)
    {
        super(storeMap);
        _baseReflector = base;
    }

    public void setConfiguration(Configuration configuration)
    {
        _configuration = configuration;
    }

    public Configuration getConfiguration()
    {
        return _configuration;

    }

    public Set<String> getTypesAnnotatedWith(String annotationClassName) throws ClassNotFoundException {
        Set<String> result = new HashSet<String>();
        Set<Class<?>> data = _baseReflector.getTypesAnnotatedWith(Class.forName(annotationClassName).asSubclass(Annotation.class));

        for (Class<?> cls : data)
        {
            result.add(cls.getName());
        }
        return result;
    }

    public Set <String>  getMethodsAnnotatedWith(String annotationClassName) throws ClassNotFoundException
    {
        Set<String> result = new HashSet<String>();
        Set<Method> data = _baseReflector.getMethodsAnnotatedWith(Class.forName(annotationClassName).asSubclass(Annotation.class));

        for (Method method : data)
        {
            result.add(method.getName());
        }
        return result;
    }

    public Set<String> getConstructorsAnnotatedWith(String annotationClassName) throws ClassNotFoundException {
        Set<String> result = new HashSet<String>();
        Set<Constructor> data = _baseReflector.getConstructorsAnnotatedWith(Class.forName(annotationClassName).asSubclass(Annotation.class));

        for (Constructor constructor : data) {
            result.add(constructor.getName());
        }
        return result;
    }

    public Set<String> getFieldsAnnotatedWith(String annotationClassName) throws ClassNotFoundException
    {
        Set<String> result = new HashSet<String>();
        Set<Field> data = _baseReflector.getFieldsAnnotatedWith(Class.forName(annotationClassName).asSubclass(Annotation.class));

        for (Field fld : data)
        {
            result.add(fld.getName());
        }
        return result;
    }

    public Reflector getBaseReflector()
    {
        return _baseReflector;
    }

    ClassLoader[] loaders() { return _configuration.getClassLoaders(); }
}
