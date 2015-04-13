package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import java.util.*;
import java.io.*;

public abstract class BaseResourceCollectionContainer extends DataType implements ResourceCollection, Cloneable
{
    private List<ResourceCollection> rc;
    private Collection<Resource> coll;
    private boolean cache;
    
    public BaseResourceCollectionContainer() {
        super();
        this.rc = new ArrayList<ResourceCollection>();
        this.coll = null;
        this.cache = true;
    }
    
    public BaseResourceCollectionContainer(final Project project) {
        super();
        this.rc = new ArrayList<ResourceCollection>();
        this.coll = null;
        this.cache = true;
        this.setProject(project);
    }
    
    public synchronized void setCache(final boolean b) {
        this.cache = b;
    }
    
    public synchronized boolean isCache() {
        return this.cache;
    }
    
    public synchronized void clear() throws BuildException {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.rc.clear();
        FailFast.invalidate(this);
        this.coll = null;
        this.setChecked(false);
    }
    
    public synchronized void add(final ResourceCollection c) throws BuildException {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        if (c == null) {
            return;
        }
        if (Project.getProject(c) == null) {
            final Project p = this.getProject();
            if (p != null) {
                p.setProjectReference(c);
            }
        }
        this.rc.add(c);
        FailFast.invalidate(this);
        this.coll = null;
        this.setChecked(false);
    }
    
    public synchronized void addAll(final Collection<? extends ResourceCollection> c) throws BuildException {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        try {
            for (final ResourceCollection resourceCollection : c) {
                this.add(resourceCollection);
            }
        }
        catch (ClassCastException e) {
            throw new BuildException(e);
        }
    }
    
    public final synchronized Iterator<Resource> iterator() {
        if (this.isReference()) {
            return ((BaseResourceCollectionContainer)this.getCheckedRef()).iterator();
        }
        this.dieOnCircularReference();
        return new FailFast(this, this.cacheCollection().iterator());
    }
    
    public synchronized int size() {
        if (this.isReference()) {
            return this.getCheckedRef(BaseResourceCollectionContainer.class, this.getDataTypeName()).size();
        }
        this.dieOnCircularReference();
        return this.cacheCollection().size();
    }
    
    public synchronized boolean isFilesystemOnly() {
        if (this.isReference()) {
            return ((BaseResourceCollectionContainer)this.getCheckedRef()).isFilesystemOnly();
        }
        this.dieOnCircularReference();
        boolean goEarly = true;
        for (Iterator<ResourceCollection> i = this.rc.iterator(); goEarly && i.hasNext(); goEarly = i.next().isFilesystemOnly()) {}
        if (goEarly) {
            return true;
        }
        for (final Resource r : this.cacheCollection()) {
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
            for (final ResourceCollection resourceCollection : this.rc) {
                if (resourceCollection instanceof DataType) {
                    DataType.pushAndInvokeCircularReferenceCheck((DataType)resourceCollection, stk, p);
                }
            }
            this.setChecked(true);
        }
    }
    
    public final synchronized List<ResourceCollection> getResourceCollections() {
        this.dieOnCircularReference();
        return Collections.unmodifiableList((List<? extends ResourceCollection>)this.rc);
    }
    
    protected abstract Collection<Resource> getCollection();
    
    public Object clone() {
        try {
            final BaseResourceCollectionContainer c = (BaseResourceCollectionContainer)super.clone();
            c.rc = new ArrayList<ResourceCollection>(this.rc);
            c.coll = null;
            return c;
        }
        catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }
    
    public synchronized String toString() {
        if (this.isReference()) {
            return this.getCheckedRef().toString();
        }
        if (this.cacheCollection().size() == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final Resource resource : this.coll) {
            if (sb.length() > 0) {
                sb.append(File.pathSeparatorChar);
            }
            sb.append(resource);
        }
        return sb.toString();
    }
    
    private synchronized Collection<Resource> cacheCollection() {
        if (this.coll == null || !this.isCache()) {
            this.coll = this.getCollection();
        }
        return this.coll;
    }
}
