package org.apache.tools.ant.types;

import java.util.*;
import org.apache.tools.ant.*;

public final class AntFilterReader extends DataType implements Cloneable
{
    private String className;
    private final Vector<Parameter> parameters;
    private Path classpath;
    
    public AntFilterReader() {
        super();
        this.parameters = new Vector<Parameter>();
    }
    
    public void setClassName(final String className) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.className = className;
    }
    
    public String getClassName() {
        if (this.isReference()) {
            return ((AntFilterReader)this.getCheckedRef()).getClassName();
        }
        this.dieOnCircularReference();
        return this.className;
    }
    
    public void addParam(final Parameter param) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.parameters.addElement(param);
    }
    
    public void setClasspath(final Path classpath) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (this.classpath == null) {
            this.classpath = classpath;
        }
        else {
            this.classpath.append(classpath);
        }
        this.setChecked(false);
    }
    
    public Path createClasspath() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        if (this.classpath == null) {
            this.classpath = new Path(this.getProject());
        }
        this.setChecked(false);
        return this.classpath.createPath();
    }
    
    public Path getClasspath() {
        if (this.isReference()) {
            ((AntFilterReader)this.getCheckedRef()).getClasspath();
        }
        this.dieOnCircularReference();
        return this.classpath;
    }
    
    public void setClasspathRef(final Reference r) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.createClasspath().setRefid(r);
    }
    
    public Parameter[] getParams() {
        if (this.isReference()) {
            ((AntFilterReader)this.getCheckedRef()).getParams();
        }
        this.dieOnCircularReference();
        final Parameter[] params = new Parameter[this.parameters.size()];
        this.parameters.copyInto(params);
        return params;
    }
    
    public void setRefid(final Reference r) throws BuildException {
        if (!this.parameters.isEmpty() || this.className != null || this.classpath != null) {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    protected synchronized void dieOnCircularReference(final Stack<Object> stk, final Project p) throws BuildException {
        if (this.isChecked()) {
            return;
        }
        if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
        }
        else {
            if (this.classpath != null) {
                DataType.pushAndInvokeCircularReferenceCheck(this.classpath, stk, p);
            }
            this.setChecked(true);
        }
    }
}
