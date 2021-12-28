package org.fudgemsg;

import org.fudgemsg.reflector.Reflector;
import org.reflections.Configuration;
import org.reflections.Store;
import org.reflections.scanners.Scanner;
import org.scannotation.AnnotationDB;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Set;

public class AnnotationReflector {

    public static final String DEFAULT_ANNOTATION_REFLECTOR_FILTER = "+annotation";

    Reflector _reflector;
    private static AnnotationReflector _defaultInstance = new AnnotationReflector(Annotation.class);

    public AnnotationReflector(Configuration configuration)
    {
        _reflector = new Reflector(configuration);
    }

    public AnnotationReflector(Store store)
    {
        _reflector = new Reflector(store);
    }

    public AnnotationReflector(Object... params)
    {
        _reflector = new Reflector(params);
    }

    public AnnotationReflector(String prefix, Scanner... scanners)
    {
        _reflector = new Reflector(prefix, scanners);
    }

    public static void initDefaultReflector(AnnotationReflector defaultReflector)
    {
        _defaultInstance = defaultReflector;
    }

    public static AnnotationReflector getDefaultReflector() {
        return _defaultInstance;
    }

    public static AnnotationReflector getClassReflector(Class<? extends Annotation> clazz) {
        return new AnnotationReflector(clazz);
    }

    public Reflector getReflector()
    {
        return _reflector;
    }
}
