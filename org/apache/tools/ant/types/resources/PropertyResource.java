package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import java.io.*;
import org.apache.tools.ant.util.*;

public class PropertyResource extends Resource
{
    private static final int PROPERTY_MAGIC;
    private static final InputStream UNSET;
    
    public PropertyResource() {
        super();
    }
    
    public PropertyResource(final Project p, final String n) {
        super(n);
        this.setProject(p);
    }
    
    public String getValue() {
        if (this.isReference()) {
            return ((PropertyResource)this.getCheckedRef()).getValue();
        }
        final Project p = this.getProject();
        return (p == null) ? null : p.getProperty(this.getName());
    }
    
    public Object getObjectValue() {
        if (this.isReference()) {
            return ((PropertyResource)this.getCheckedRef()).getObjectValue();
        }
        final Project p = this.getProject();
        return (p == null) ? null : PropertyHelper.getProperty(p, this.getName());
    }
    
    public boolean isExists() {
        if (this.isReferenceOrProxy()) {
            return this.getReferencedOrProxied().isExists();
        }
        return this.getObjectValue() != null;
    }
    
    public long getSize() {
        if (this.isReferenceOrProxy()) {
            return this.getReferencedOrProxied().getSize();
        }
        final Object o = this.getObjectValue();
        return (o == null) ? 0L : String.valueOf(o).length();
    }
    
    public boolean equals(final Object o) {
        return super.equals(o) || (this.isReferenceOrProxy() && this.getReferencedOrProxied().equals(o));
    }
    
    public int hashCode() {
        if (this.isReferenceOrProxy()) {
            return this.getReferencedOrProxied().hashCode();
        }
        return super.hashCode() * PropertyResource.PROPERTY_MAGIC;
    }
    
    public String toString() {
        if (this.isReferenceOrProxy()) {
            return this.getReferencedOrProxied().toString();
        }
        return this.getValue();
    }
    
    public InputStream getInputStream() throws IOException {
        if (this.isReferenceOrProxy()) {
            return this.getReferencedOrProxied().getInputStream();
        }
        final Object o = this.getObjectValue();
        return (o == null) ? PropertyResource.UNSET : new ByteArrayInputStream(String.valueOf(o).getBytes());
    }
    
    public OutputStream getOutputStream() throws IOException {
        if (this.isReferenceOrProxy()) {
            return this.getReferencedOrProxied().getOutputStream();
        }
        if (this.isExists()) {
            throw new ImmutableResourceException();
        }
        return new PropertyOutputStream(this.getProject(), this.getName());
    }
    
    protected boolean isReferenceOrProxy() {
        return this.isReference() || this.getObjectValue() instanceof Resource;
    }
    
    protected Resource getReferencedOrProxied() {
        if (this.isReference()) {
            return this.getCheckedRef(Resource.class, "resource");
        }
        final Object o = this.getObjectValue();
        if (o instanceof Resource) {
            return (Resource)o;
        }
        throw new IllegalStateException("This PropertyResource does not reference or proxy another Resource");
    }
    
    static {
        PROPERTY_MAGIC = Resource.getMagicNumber("PropertyResource".getBytes());
        UNSET = new InputStream() {
            public int read() {
                return -1;
            }
        };
    }
}
