package org.apache.tools.ant;

public interface TypeAdapter
{
    void setProject(Project p0);
    
    Project getProject();
    
    void setProxy(Object p0);
    
    Object getProxy();
    
    void checkProxyClass(Class<?> p0);
}
