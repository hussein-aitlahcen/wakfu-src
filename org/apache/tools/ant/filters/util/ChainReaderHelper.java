package org.apache.tools.ant.filters.util;

import org.apache.tools.ant.*;
import org.apache.tools.ant.util.*;
import java.io.*;
import org.apache.tools.ant.filters.*;
import java.util.*;
import java.lang.reflect.*;
import org.apache.tools.ant.types.*;

public final class ChainReaderHelper
{
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    public Reader primaryReader;
    public int bufferSize;
    public Vector<FilterChain> filterChains;
    private Project project;
    
    public ChainReaderHelper() {
        super();
        this.bufferSize = 8192;
        this.filterChains = new Vector<FilterChain>();
        this.project = null;
    }
    
    public void setPrimaryReader(final Reader rdr) {
        this.primaryReader = rdr;
    }
    
    public void setProject(final Project project) {
        this.project = project;
    }
    
    public Project getProject() {
        return this.project;
    }
    
    public void setBufferSize(final int size) {
        this.bufferSize = size;
    }
    
    public void setFilterChains(final Vector<FilterChain> fchain) {
        this.filterChains = fchain;
    }
    
    public Reader getAssembledReader() throws BuildException {
        if (this.primaryReader == null) {
            throw new BuildException("primaryReader must not be null.");
        }
        Reader instream = this.primaryReader;
        final int filterReadersCount = this.filterChains.size();
        final Vector<Object> finalFilters = new Vector<Object>();
        final ArrayList<AntClassLoader> classLoadersToCleanUp = new ArrayList<AntClassLoader>();
        for (int i = 0; i < filterReadersCount; ++i) {
            final FilterChain filterchain = this.filterChains.elementAt(i);
            final Vector<Object> filterReaders = filterchain.getFilterReaders();
            for (int readerCount = filterReaders.size(), j = 0; j < readerCount; ++j) {
                finalFilters.addElement(filterReaders.elementAt(j));
            }
        }
        final int filtersCount = finalFilters.size();
        if (filtersCount > 0) {
            boolean success = false;
            try {
                for (int k = 0; k < filtersCount; ++k) {
                    final Object o = finalFilters.elementAt(k);
                    if (o instanceof AntFilterReader) {
                        instream = this.expandReader(finalFilters.elementAt(k), instream, classLoadersToCleanUp);
                    }
                    else if (o instanceof ChainableReader) {
                        this.setProjectOnObject(o);
                        instream = ((ChainableReader)o).chain(instream);
                        this.setProjectOnObject(instream);
                    }
                }
                success = true;
            }
            finally {
                if (!success && classLoadersToCleanUp.size() > 0) {
                    cleanUpClassLoaders(classLoadersToCleanUp);
                }
            }
        }
        final Reader finalReader = instream;
        return (classLoadersToCleanUp.size() == 0) ? finalReader : new FilterReader(finalReader) {
            public void close() throws IOException {
                FileUtils.close(this.in);
                cleanUpClassLoaders(classLoadersToCleanUp);
            }
            
            protected void finalize() throws Throwable {
                try {
                    this.close();
                }
                finally {
                    super.finalize();
                }
            }
        };
    }
    
    private void setProjectOnObject(final Object obj) {
        if (this.project == null) {
            return;
        }
        if (obj instanceof BaseFilterReader) {
            ((BaseFilterReader)obj).setProject(this.project);
            return;
        }
        this.project.setProjectReference(obj);
    }
    
    private static void cleanUpClassLoaders(final List<AntClassLoader> loaders) {
        final Iterator<AntClassLoader> it = loaders.iterator();
        while (it.hasNext()) {
            it.next().cleanup();
        }
    }
    
    public String readFully(final Reader rdr) throws IOException {
        return FileUtils.readFully(rdr, this.bufferSize);
    }
    
    private Reader expandReader(final AntFilterReader filter, final Reader ancestor, final List<AntClassLoader> classLoadersToCleanUp) {
        final String className = filter.getClassName();
        final Path classpath = filter.getClasspath();
        final Project pro = filter.getProject();
        if (className != null) {
            try {
                Class<?> clazz = null;
                if (classpath == null) {
                    clazz = Class.forName(className);
                }
                else {
                    final AntClassLoader al = pro.createClassLoader(classpath);
                    classLoadersToCleanUp.add(al);
                    clazz = Class.forName(className, true, al);
                }
                if (clazz != null) {
                    if (!FilterReader.class.isAssignableFrom(clazz)) {
                        throw new BuildException(className + " does not extend" + " java.io.FilterReader");
                    }
                    final Constructor<?>[] constructors = clazz.getConstructors();
                    int j = 0;
                    boolean consPresent = false;
                    while (j < constructors.length) {
                        final Class<?>[] types = constructors[j].getParameterTypes();
                        if (types.length == 1 && types[0].isAssignableFrom(Reader.class)) {
                            consPresent = true;
                            break;
                        }
                        ++j;
                    }
                    if (!consPresent) {
                        throw new BuildException(className + " does not define" + " a public constructor" + " that takes in a Reader" + " as its single argument.");
                    }
                    final Reader[] rdr = { ancestor };
                    final Reader instream = (Reader)constructors[j].newInstance((Object[])rdr);
                    this.setProjectOnObject(instream);
                    if (Parameterizable.class.isAssignableFrom(clazz)) {
                        final Parameter[] params = filter.getParams();
                        ((Parameterizable)instream).setParameters(params);
                    }
                    return instream;
                }
            }
            catch (ClassNotFoundException cnfe) {
                throw new BuildException(cnfe);
            }
            catch (InstantiationException ie) {
                throw new BuildException(ie);
            }
            catch (IllegalAccessException iae) {
                throw new BuildException(iae);
            }
            catch (InvocationTargetException ite) {
                throw new BuildException(ite);
            }
        }
        return ancestor;
    }
}
