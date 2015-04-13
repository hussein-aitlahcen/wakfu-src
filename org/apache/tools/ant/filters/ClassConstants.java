package org.apache.tools.ant.filters;

import org.apache.tools.ant.*;
import java.lang.reflect.*;
import java.io.*;

public final class ClassConstants extends BaseFilterReader implements ChainableReader
{
    private String queuedData;
    private static final String JAVA_CLASS_HELPER = "org.apache.tools.ant.filters.util.JavaClassHelper";
    
    public ClassConstants() {
        super();
        this.queuedData = null;
    }
    
    public ClassConstants(final Reader in) {
        super(in);
        this.queuedData = null;
    }
    
    public int read() throws IOException {
        int ch = -1;
        if (this.queuedData != null && this.queuedData.length() == 0) {
            this.queuedData = null;
        }
        if (this.queuedData != null) {
            ch = this.queuedData.charAt(0);
            this.queuedData = this.queuedData.substring(1);
            if (this.queuedData.length() == 0) {
                this.queuedData = null;
            }
        }
        else {
            final String clazz = this.readFully();
            if (clazz == null || clazz.length() == 0) {
                ch = -1;
            }
            else {
                final byte[] bytes = clazz.getBytes("ISO-8859-1");
                try {
                    final Class<?> javaClassHelper = Class.forName("org.apache.tools.ant.filters.util.JavaClassHelper");
                    if (javaClassHelper != null) {
                        final Class<?>[] params = (Class<?>[])new Class[] { byte[].class };
                        final Method getConstants = javaClassHelper.getMethod("getConstants", params);
                        final Object[] args = { bytes };
                        final StringBuffer sb = (StringBuffer)getConstants.invoke(null, args);
                        if (sb.length() > 0) {
                            this.queuedData = sb.toString();
                            return this.read();
                        }
                    }
                }
                catch (NoClassDefFoundError ex) {
                    throw ex;
                }
                catch (RuntimeException ex2) {
                    throw ex2;
                }
                catch (InvocationTargetException ex3) {
                    final Throwable t = ex3.getTargetException();
                    if (t instanceof NoClassDefFoundError) {
                        throw (NoClassDefFoundError)t;
                    }
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    }
                    throw new BuildException(t);
                }
                catch (Exception ex4) {
                    throw new BuildException(ex4);
                }
            }
        }
        return ch;
    }
    
    public Reader chain(final Reader rdr) {
        final ClassConstants newFilter = new ClassConstants(rdr);
        return newFilter;
    }
}
