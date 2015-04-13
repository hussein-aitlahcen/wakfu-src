package org.apache.tools.ant.types.selectors.modifiedselector;

import java.util.*;

public interface Cache
{
    boolean isValid();
    
    void delete();
    
    void load();
    
    void save();
    
    Object get(Object p0);
    
    void put(Object p0, Object p1);
    
    Iterator<String> iterator();
}
