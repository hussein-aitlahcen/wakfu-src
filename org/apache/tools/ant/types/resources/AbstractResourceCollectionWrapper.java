package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.util.*;
import java.io.*;

public abstract class AbstractResourceCollectionWrapper extends DataType implements ResourceCollection, Cloneable
{
    private static final String ONE_NESTED_MESSAGE = " expects exactly one nested resource collection.";
    private ResourceCollection rc;
    private boolean cache;
    
    public AbstractResourceCollectionWrapper() {
        super();
        this.cache = true;
    }
    
    public synchronized void setCache(final boolean b) {
        this.cache = b;
    }
    
    public synchronized boolean isCache() {
        return this.cache;
    }
    
    public synchronized void add(final ResourceCollection c) throws BuildException {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        if (c == null) {
            return;
        }
        if (this.rc != null) {
            throw this.oneNested();
        }
        this.rc = c;
        if (Project.getProject(this.rc) == null) {
            final Project p = this.getProject();
            if (p != null) {
                p.setProjectReference(this.rc);
            }
        }
        this.setChecked(false);
    }
    
    public final synchronized Iterator<Resource> iterator() {
        if (this.isReference()) {
            return ((AbstractResourceCollectionWrapper)this.getCheckedRef()).iterator();
        }
        this.dieOnCircularReference();
        return new FailFast(this, this.createIterator());
    }
    
    protected abstract Iterator<Resource> createIterator();
    
    public synchronized int size() {
        if (this.isReference()) {
            return ((AbstractResourceCollectionWrapper)this.getCheckedRef()).size();
        }
        this.dieOnCircularReference();
        return this.getSize();
    }
    
    protected abstract int getSize();
    
    public synchronized boolean isFilesystemOnly() {
        if (this.isReference()) {
            return ((BaseResourceCollectionContainer)this.getCheckedRef()).isFilesystemOnly();
        }
        this.dieOnCircularReference();
        if (this.rc == null || this.rc.isFilesystemOnly()) {
            return true;
        }
        for (final Resource r : this) {
            if (r.as(FileProvider.class) == null) {
                return false;
            }
        }
        return true;
    }
    
    protected synchronized void dieOnCircularReference(final Stack<Object> stk, final Project p) throws BuildException {
        if (this.isChecked()) {
            return;
        }
        if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
        }
        else {
            if (this.rc instanceof DataType) {
                DataType.pushAndInvokeCircularReferenceCheck((DataType)this.rc, stk, p);
            }
            this.setChecked(true);
        }
    }
    
    protected final synchronized ResourceCollection getResourceCollection() {
        this.dieOnCircularReference();
        if (this.rc == null) {
            throw this.oneNested();
        }
        return this.rc;
    }
    
    public synchronized String toString() {
        if (this.isReference()) {
            return this.getCheckedRef().toString();
        }
        if (this.getSize() == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final Resource resource : this) {
            if (sb.length() > 0) {
                sb.append(File.pathSeparatorChar);
            }
            sb.append(resource);
        }
        return sb.toString();
    }
    
    private BuildException oneNested() {
        return new BuildException(super.toString() + " expects exactly one nested resource collection.");
    }
}
