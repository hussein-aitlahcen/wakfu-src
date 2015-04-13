package org.apache.commons.pool;

public interface PoolableObjectFactory
{
    Object makeObject() throws Exception;
    
    void destroyObject(Object p0) throws Exception;
    
    boolean validateObject(Object p0);
    
    void activateObject(Object p0) throws Exception;
    
    void passivateObject(Object p0) throws Exception;
}
