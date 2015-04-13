package org.apache.commons.pool;

public interface ObjectPool
{
    Object borrowObject() throws Exception;
    
    void returnObject(Object p0) throws Exception;
    
    void invalidateObject(Object p0) throws Exception;
    
    void addObject() throws Exception;
    
    int getNumIdle() throws UnsupportedOperationException;
    
    int getNumActive() throws UnsupportedOperationException;
    
    void clear() throws Exception, UnsupportedOperationException;
    
    void close() throws Exception;
    
    void setFactory(PoolableObjectFactory p0) throws IllegalStateException, UnsupportedOperationException;
}
