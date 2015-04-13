package org.apache.tools.ant.util;

import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.launch.*;

public class LoaderUtils
{
    private static final FileUtils FILE_UTILS;
    
    public static void setContextClassLoader(final ClassLoader loader) {
        final Thread currentThread = Thread.currentThread();
        currentThread.setContextClassLoader(loader);
    }
    
    public static ClassLoader getContextClassLoader() {
        final Thread currentThread = Thread.currentThread();
        return currentThread.getContextClassLoader();
    }
    
    public static boolean isContextLoaderAvailable() {
        return true;
    }
    
    private static File normalizeSource(File source) {
        if (source != null) {
            try {
                source = LoaderUtils.FILE_UTILS.normalize(source.getAbsolutePath());
            }
            catch (BuildException ex) {}
        }
        return source;
    }
    
    public static File getClassSource(final Class c) {
        return normalizeSource(Locator.getClassSource(c));
    }
    
    public static File getResourceSource(ClassLoader c, final String resource) {
        if (c == null) {
            c = LoaderUtils.class.getClassLoader();
        }
        return normalizeSource(Locator.getResourceSource(c, resource));
    }
    
    public static String classNameToResource(final String className) {
        return className.replace('.', '/') + ".class";
    }
    
    public static boolean classExists(final ClassLoader loader, final String className) {
        return loader.getResource(classNameToResource(className)) != null;
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
}
