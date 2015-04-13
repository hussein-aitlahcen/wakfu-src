package org.apache.tools.ant.types.selectors;

import java.util.*;
import org.apache.tools.ant.types.*;
import java.io.*;
import org.apache.tools.ant.*;

public class ExtendSelector extends BaseSelector
{
    private String classname;
    private FileSelector dynselector;
    private Vector<Parameter> paramVec;
    private Path classpath;
    
    public ExtendSelector() {
        super();
        this.classname = null;
        this.dynselector = null;
        this.paramVec = new Vector<Parameter>();
        this.classpath = null;
    }
    
    public void setClassname(final String classname) {
        this.classname = classname;
    }
    
    public void selectorCreate() {
        if (this.classname != null && this.classname.length() > 0) {
            try {
                Class<?> c = null;
                if (this.classpath == null) {
                    c = Class.forName(this.classname);
                }
                else {
                    final AntClassLoader al = this.getProject().createClassLoader(this.classpath);
                    c = Class.forName(this.classname, true, al);
                }
                this.dynselector = (FileSelector)c.asSubclass(FileSelector.class).newInstance();
                final Project p = this.getProject();
                if (p != null) {
                    p.setProjectReference(this.dynselector);
                }
            }
            catch (ClassNotFoundException cnfexcept) {
                this.setError("Selector " + this.classname + " not initialized, no such class");
            }
            catch (InstantiationException iexcept) {
                this.setError("Selector " + this.classname + " not initialized, could not create class");
            }
            catch (IllegalAccessException iaexcept) {
                this.setError("Selector " + this.classname + " not initialized, class not accessible");
            }
        }
        else {
            this.setError("There is no classname specified");
        }
    }
    
    public void addParam(final Parameter p) {
        this.paramVec.addElement(p);
    }
    
    public final void setClasspath(final Path classpath) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (this.classpath == null) {
            this.classpath = classpath;
        }
        else {
            this.classpath.append(classpath);
        }
    }
    
    public final Path createClasspath() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        if (this.classpath == null) {
            this.classpath = new Path(this.getProject());
        }
        return this.classpath.createPath();
    }
    
    public final Path getClasspath() {
        return this.classpath;
    }
    
    public void setClasspathref(final Reference r) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.createClasspath().setRefid(r);
    }
    
    public void verifySettings() {
        if (this.dynselector == null) {
            this.selectorCreate();
        }
        if (this.classname == null || this.classname.length() < 1) {
            this.setError("The classname attribute is required");
        }
        else if (this.dynselector == null) {
            this.setError("Internal Error: The custom selector was not created");
        }
        else if (!(this.dynselector instanceof ExtendFileSelector) && this.paramVec.size() > 0) {
            this.setError("Cannot set parameters on custom selector that does not implement ExtendFileSelector");
        }
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) throws BuildException {
        this.validate();
        if (this.paramVec.size() > 0 && this.dynselector instanceof ExtendFileSelector) {
            final Parameter[] paramArray = new Parameter[this.paramVec.size()];
            this.paramVec.copyInto(paramArray);
            ((ExtendFileSelector)this.dynselector).setParameters(paramArray);
        }
        return this.dynselector.isSelected(basedir, filename, file);
    }
}
