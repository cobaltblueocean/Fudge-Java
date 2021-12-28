package org.fudgemsg.reflector;

import org.reflections.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class ConfigurationBuilder extends org.reflections.util.ConfigurationBuilder {
    private ExecutorService executorService;

    public ConfigurationBuilder addClassLoaders(ClassLoader[] classLoaders)
    {
        ClassLoader[] loaders = getClassLoaders();
        Set<ClassLoader> newLoaders = new HashSet<ClassLoader>();
        newLoaders.addAll(Arrays.asList(getClassLoaders()));
        newLoaders.addAll(Arrays.asList(classLoaders));

        return (ConfigurationBuilder)setClassLoaders((ClassLoader[])newLoaders.toArray());
    }

    /** sets the executor service used for scanning. */
    public ConfigurationBuilder setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
