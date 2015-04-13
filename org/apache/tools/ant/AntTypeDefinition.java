package org.apache.tools.ant;

import java.lang.reflect.*;

public class AntTypeDefinition
{
    private String name;
    private Class<?> clazz;
    private Class<?> adapterClass;
    private Class<?> adaptToClass;
    private String className;
    private ClassLoader classLoader;
    private boolean restrict;
    
    public AntTypeDefinition() {
        super();
        this.restrict = false;
    }
    
    public void setRestrict(final boolean restrict) {
        this.restrict = restrict;
    }
    
    public boolean isRestrict() {
        return this.restrict;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setClass(final Class<?> clazz) {
        this.clazz = clazz;
        if (clazz == null) {
            return;
        }
        this.classLoader = ((this.classLoader == null) ? clazz.getClassLoader() : this.classLoader);
        this.className = ((this.className == null) ? clazz.getName() : this.className);
    }
    
    public void setClassName(final String className) {
        this.className = className;
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public void setAdapterClass(final Class<?> adapterClass) {
        this.adapterClass = adapterClass;
    }
    
    public void setAdaptToClass(final Class<?> adaptToClass) {
        this.adaptToClass = adaptToClass;
    }
    
    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    public Class<?> getExposedClass(final Project project) {
        if (this.adaptToClass != null) {
            final Class<?> z = this.getTypeClass(project);
            if (z == null || this.adaptToClass.isAssignableFrom(z)) {
                return z;
            }
        }
        return (this.adapterClass == null) ? this.getTypeClass(project) : this.adapterClass;
    }
    
    public Class<?> getTypeClass(final Project project) {
        try {
            return this.innerGetTypeClass();
        }
        catch (NoClassDefFoundError ncdfe) {
            project.log("Could not load a dependent class (" + ncdfe.getMessage() + ") for type " + this.name, 4);
        }
        catch (ClassNotFoundException cnfe) {
            project.log("Could not load class (" + this.className + ") for type " + this.name, 4);
        }
        return null;
    }
    
    public Class<?> innerGetTypeClass() throws ClassNotFoundException {
        if (this.clazz != null) {
            return this.clazz;
        }
        if (this.classLoader == null) {
            this.clazz = Class.forName(this.className);
        }
        else {
            this.clazz = this.classLoader.loadClass(this.className);
        }
        return this.clazz;
    }
    
    public Object create(final Project project) {
        return this.icreate(project);
    }
    
    private Object icreate(final Project project) {
        final Class<?> c = this.getTypeClass(project);
        if (c == null) {
            return null;
        }
        final Object o = this.createAndSet(project, c);
        if (o == null || this.adapterClass == null) {
            return o;
        }
        if (this.adaptToClass != null && this.adaptToClass.isAssignableFrom(o.getClass())) {
            return o;
        }
        final TypeAdapter adapterObject = (TypeAdapter)this.createAndSet(project, this.adapterClass);
        if (adapterObject == null) {
            return null;
        }
        adapterObject.setProxy(o);
        return adapterObject;
    }
    
    public void checkClass(final Project project) {
        if (this.clazz == null) {
            this.clazz = this.getTypeClass(project);
            if (this.clazz == null) {
                throw new BuildException("Unable to create class for " + this.getName());
            }
        }
        if (this.adapterClass != null && (this.adaptToClass == null || !this.adaptToClass.isAssignableFrom(this.clazz))) {
            final TypeAdapter adapter = (TypeAdapter)this.createAndSet(project, this.adapterClass);
            if (adapter == null) {
                throw new BuildException("Unable to create adapter object");
            }
            adapter.checkProxyClass(this.clazz);
        }
    }
    
    private Object createAndSet(final Project project, final Class<?> c) {
        try {
            final Object o = this.innerCreateAndSet(c, project);
            return o;
        }
        catch (InvocationTargetException ex) {
            final Throwable t = ex.getTargetException();
            throw new BuildException("Could not create type " + this.name + " due to " + t, t);
        }
        catch (NoClassDefFoundError ncdfe) {
            final String msg = "Type " + this.name + ": A class needed by class " + c + " cannot be found: " + ncdfe.getMessage();
            throw new BuildException(msg, ncdfe);
        }
        catch (NoSuchMethodException nsme) {
            throw new BuildException("Could not create type " + this.name + " as the class " + c + " has no compatible constructor");
        }
        catch (InstantiationException nsme2) {
            throw new BuildException("Could not create type " + this.name + " as the class " + c + " is abstract");
        }
        catch (IllegalAccessException e) {
            throw new BuildException("Could not create type " + this.name + " as the constructor " + c + " is not accessible");
        }
        catch (Throwable t2) {
            throw new BuildException("Could not create type " + this.name + " due to " + t2, t2);
        }
    }
    
    public <T> T innerCreateAndSet(final Class<T> newclass, final Project project) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        boolean noArg = false;
        Constructor<T> ctor;
        try {
            ctor = newclass.getConstructor((Class<?>[])new Class[0]);
            noArg = true;
        }
        catch (NoSuchMethodException nse) {
            ctor = newclass.getConstructor(Project.class);
            noArg = false;
        }
        final Constructor<T> constructor = ctor;
        final Object[] initargs;
        if (noArg) {
            final Object[] array = new Object[0];
        }
        else {
            initargs = new Object[] { project };
        }
        final T o = constructor.newInstance(initargs);
        project.setProjectReference(o);
        return o;
    }
    
    public boolean sameDefinition(final AntTypeDefinition other, final Project project) {
        return other != null && other.getClass() == this.getClass() && other.getTypeClass(project).equals(this.getTypeClass(project)) && other.getExposedClass(project).equals(this.getExposedClass(project)) && other.restrict == this.restrict && other.adapterClass == this.adapterClass && other.adaptToClass == this.adaptToClass;
    }
    
    public boolean similarDefinition(final AntTypeDefinition other, final Project project) {
        if (other == null || this.getClass() != other.getClass() || !this.getClassName().equals(other.getClassName()) || !this.extractClassname(this.adapterClass).equals(this.extractClassname(other.adapterClass)) || !this.extractClassname(this.adaptToClass).equals(this.extractClassname(other.adaptToClass)) || this.restrict != other.restrict) {
            return false;
        }
        final ClassLoader oldLoader = other.getClassLoader();
        final ClassLoader newLoader = this.getClassLoader();
        return oldLoader == newLoader || (oldLoader instanceof AntClassLoader && newLoader instanceof AntClassLoader && ((AntClassLoader)oldLoader).getClasspath().equals(((AntClassLoader)newLoader).getClasspath()));
    }
    
    private String extractClassname(final Class<?> c) {
        return (c == null) ? "<null>" : c.getClass().getName();
    }
}
