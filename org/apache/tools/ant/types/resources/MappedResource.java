package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.types.*;

public class MappedResource extends ResourceDecorator
{
    private final FileNameMapper mapper;
    
    public MappedResource(final Resource r, final FileNameMapper m) {
        super(r);
        this.mapper = m;
    }
    
    public String getName() {
        final String name = this.getResource().getName();
        if (this.isReference()) {
            return name;
        }
        final String[] mapped = this.mapper.mapFileName(name);
        return (mapped != null && mapped.length > 0) ? mapped[0] : null;
    }
    
    public void setRefid(final Reference r) {
        if (this.mapper != null) {
            throw this.noChildrenAllowed();
        }
        super.setRefid(r);
    }
    
    public <T> T as(final Class<T> clazz) {
        return FileProvider.class.isAssignableFrom(clazz) ? null : this.getResource().as(clazz);
    }
    
    public int hashCode() {
        final String n = this.getName();
        return (n == null) ? super.hashCode() : n.hashCode();
    }
    
    public boolean equals(final Object other) {
        if (other == null || !other.getClass().equals(this.getClass())) {
            return false;
        }
        final MappedResource m = (MappedResource)other;
        final String myName = this.getName();
        final String otherName = m.getName();
        if (myName == null) {
            if (otherName != null) {
                return false;
            }
        }
        else if (!myName.equals(otherName)) {
            return false;
        }
        if (this.getResource().equals(m.getResource())) {
            return true;
        }
        return false;
    }
}
