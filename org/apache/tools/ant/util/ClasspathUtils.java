package org.apache.tools.ant.util;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;

public class ClasspathUtils
{
    public static final String REUSE_LOADER_REF = "ant.reuse.loader";
    
    public static ClassLoader getClassLoaderForPath(final Project p, final Reference ref) {
        return getClassLoaderForPath(p, ref, false);
    }
    
    public static ClassLoader getClassLoaderForPath(final Project p, final Reference ref, final boolean reverseLoader) {
        final String pathId = ref.getRefId();
        final Object path = p.getReference(pathId);
        if (!(path instanceof Path)) {
            throw new BuildException("The specified classpathref " + pathId + " does not reference a Path.");
        }
        final String loaderId = "ant.loader." + pathId;
        return getClassLoaderForPath(p, (Path)path, loaderId, reverseLoader);
    }
    
    public static ClassLoader getClassLoaderForPath(final Project p, final Path path, final String loaderId) {
        return getClassLoaderForPath(p, path, loaderId, false);
    }
    
    public static ClassLoader getClassLoaderForPath(final Project p, final Path path, final String loaderId, final boolean reverseLoader) {
        return getClassLoaderForPath(p, path, loaderId, reverseLoader, isMagicPropertySet(p));
    }
    
    public static ClassLoader getClassLoaderForPath(final Project p, final Path path, final String loaderId, final boolean reverseLoader, final boolean reuseLoader) {
        ClassLoader cl = null;
        if (loaderId != null && reuseLoader) {
            final Object reusedLoader = p.getReference(loaderId);
            if (reusedLoader != null && !(reusedLoader instanceof ClassLoader)) {
                throw new BuildException("The specified loader id " + loaderId + " does not reference a class loader");
            }
            cl = (ClassLoader)reusedLoader;
        }
        if (cl == null) {
            cl = getUniqueClassLoaderForPath(p, path, reverseLoader);
            if (loaderId != null && reuseLoader) {
                p.addReference(loaderId, cl);
            }
        }
        return cl;
    }
    
    public static ClassLoader getUniqueClassLoaderForPath(final Project p, final Path path, final boolean reverseLoader) {
        final AntClassLoader acl = p.createClassLoader(path);
        if (reverseLoader) {
            acl.setParentFirst(false);
            acl.addJavaLibraries();
        }
        return acl;
    }
    
    public static Object newInstance(final String className, final ClassLoader userDefinedLoader) {
        return newInstance(className, userDefinedLoader, Object.class);
    }
    
    public static Object newInstance(final String className, final ClassLoader userDefinedLoader, final Class expectedType) {
        try {
            final Class clazz = Class.forName(className, true, userDefinedLoader);
            final Object o = clazz.newInstance();
            if (!expectedType.isInstance(o)) {
                throw new BuildException("Class of unexpected Type: " + className + " expected :" + expectedType);
            }
            return o;
        }
        catch (ClassNotFoundException e) {
            throw new BuildException("Class not found: " + className, e);
        }
        catch (InstantiationException e2) {
            throw new BuildException("Could not instantiate " + className + ". Specified class should have a no " + "argument constructor.", e2);
        }
        catch (IllegalAccessException e3) {
            throw new BuildException("Could not instantiate " + className + ". Specified class should have a " + "public constructor.", e3);
        }
        catch (LinkageError e4) {
            throw new BuildException("Class " + className + " could not be loaded because of an invalid dependency.", e4);
        }
    }
    
    public static Delegate getDelegate(final ProjectComponent component) {
        return new Delegate(component);
    }
    
    private static boolean isMagicPropertySet(final Project p) {
        return p.getProperty("ant.reuse.loader") != null;
    }
    
    public static class Delegate
    {
        private final ProjectComponent component;
        private Path classpath;
        private String classpathId;
        private String className;
        private String loaderId;
        private boolean reverseLoader;
        
        Delegate(final ProjectComponent component) {
            super();
            this.reverseLoader = false;
            this.component = component;
        }
        
        public void setClasspath(final Path classpath) {
            if (this.classpath == null) {
                this.classpath = classpath;
            }
            else {
                this.classpath.append(classpath);
            }
        }
        
        public Path createClasspath() {
            if (this.classpath == null) {
                this.classpath = new Path(this.component.getProject());
            }
            return this.classpath.createPath();
        }
        
        public void setClassname(final String fcqn) {
            this.className = fcqn;
        }
        
        public void setClasspathref(final Reference r) {
            this.classpathId = r.getRefId();
            this.createClasspath().setRefid(r);
        }
        
        public void setReverseLoader(final boolean reverseLoader) {
            this.reverseLoader = reverseLoader;
        }
        
        public void setLoaderRef(final Reference r) {
            this.loaderId = r.getRefId();
        }
        
        public ClassLoader getClassLoader() {
            return ClasspathUtils.getClassLoaderForPath(this.getContextProject(), this.classpath, this.getClassLoadId(), this.reverseLoader, this.loaderId != null || isMagicPropertySet(this.getContextProject()));
        }
        
        private Project getContextProject() {
            return this.component.getProject();
        }
        
        public String getClassLoadId() {
            if (this.loaderId == null && this.classpathId != null) {
                return "ant.loader." + this.classpathId;
            }
            return this.loaderId;
        }
        
        public Object newInstance() {
            return ClasspathUtils.newInstance(this.className, this.getClassLoader());
        }
        
        public Path getClasspath() {
            return this.classpath;
        }
        
        public boolean isReverseLoader() {
            return this.reverseLoader;
        }
    }
}
