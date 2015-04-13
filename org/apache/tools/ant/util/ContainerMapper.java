package org.apache.tools.ant.util;

import org.apache.tools.ant.types.*;
import java.util.*;

public abstract class ContainerMapper implements FileNameMapper
{
    private List mappers;
    
    public ContainerMapper() {
        super();
        this.mappers = new ArrayList();
    }
    
    public void addConfiguredMapper(final Mapper mapper) {
        this.add(mapper.getImplementation());
    }
    
    public void addConfigured(final FileNameMapper fileNameMapper) {
        this.add(fileNameMapper);
    }
    
    public synchronized void add(final FileNameMapper fileNameMapper) {
        if (this == fileNameMapper || (fileNameMapper instanceof ContainerMapper && ((ContainerMapper)fileNameMapper).contains(this))) {
            throw new IllegalArgumentException("Circular mapper containment condition detected");
        }
        this.mappers.add(fileNameMapper);
    }
    
    protected synchronized boolean contains(final FileNameMapper fileNameMapper) {
        boolean foundit = false;
        FileNameMapper next;
        for (Iterator iter = this.mappers.iterator(); iter.hasNext() && !foundit; foundit = (next == fileNameMapper || (next instanceof ContainerMapper && ((ContainerMapper)next).contains(fileNameMapper)))) {
            next = iter.next();
        }
        return foundit;
    }
    
    public synchronized List getMappers() {
        return Collections.unmodifiableList((List<?>)this.mappers);
    }
    
    public void setFrom(final String ignore) {
    }
    
    public void setTo(final String ignore) {
    }
}
