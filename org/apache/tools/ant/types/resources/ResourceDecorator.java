package org.apache.tools.ant.types.resources;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

public abstract class ResourceDecorator extends Resource
{
    private Resource resource;
    
    protected ResourceDecorator() {
        super();
    }
    
    protected ResourceDecorator(final ResourceCollection other) {
        super();
        this.addConfigured(other);
    }
    
    public final void addConfigured(final ResourceCollection a) {
        this.checkChildrenAllowed();
        if (this.resource != null) {
            throw new BuildException("you must not specify more than one resource");
        }
        if (a.size() != 1) {
            throw new BuildException("only single argument resource collections are supported");
        }
        this.setChecked(false);
        this.resource = a.iterator().next();
    }
    
    public String getName() {
        return this.getResource().getName();
    }
    
    public boolean isExists() {
        return this.getResource().isExists();
    }
    
    public long getLastModified() {
        return this.getResource().getLastModified();
    }
    
    public boolean isDirectory() {
        return this.getResource().isDirectory();
    }
    
    public long getSize() {
        return this.getResource().getSize();
    }
    
    public InputStream getInputStream() throws IOException {
        return this.getResource().getInputStream();
    }
    
    public OutputStream getOutputStream() throws IOException {
        return this.getResource().getOutputStream();
    }
    
    public boolean isFilesystemOnly() {
        return this.as(FileProvider.class) != null;
    }
    
    public void setRefid(final Reference r) {
        if (this.resource != null) {
            throw this.noChildrenAllowed();
        }
        super.setRefid(r);
    }
    
    public <T> T as(final Class<T> clazz) {
        return this.getResource().as(clazz);
    }
    
    public int compareTo(final Resource other) {
        if (other == this) {
            return 0;
        }
        if (other instanceof ResourceDecorator) {
            return this.getResource().compareTo(((ResourceDecorator)other).getResource());
        }
        return this.getResource().compareTo(other);
    }
    
    public int hashCode() {
        return this.getClass().hashCode() << 4 | this.getResource().hashCode();
    }
    
    protected final Resource getResource() {
        if (this.isReference()) {
            return (Resource)this.getCheckedRef();
        }
        if (this.resource == null) {
            throw new BuildException("no resource specified");
        }
        this.dieOnCircularReference();
        return this.resource;
    }
    
    protected void dieOnCircularReference(final Stack<Object> stack, final Project project) throws BuildException {
        if (this.isChecked()) {
            return;
        }
        if (this.isReference()) {
            super.dieOnCircularReference(stack, project);
        }
        else {
            DataType.pushAndInvokeCircularReferenceCheck(this.resource, stack, project);
            this.setChecked(true);
        }
    }
    
    public void setName(final String name) throws BuildException {
        throw new BuildException("you can't change the name of a " + this.getDataTypeName());
    }
    
    public void setExists(final boolean exists) {
        throw new BuildException("you can't change the exists state of a " + this.getDataTypeName());
    }
    
    public void setLastModified(final long lastmodified) throws BuildException {
        throw new BuildException("you can't change the timestamp of a " + this.getDataTypeName());
    }
    
    public void setDirectory(final boolean directory) throws BuildException {
        throw new BuildException("you can't change the directory state of a " + this.getDataTypeName());
    }
    
    public void setSize(final long size) throws BuildException {
        throw new BuildException("you can't change the size of a " + this.getDataTypeName());
    }
}
