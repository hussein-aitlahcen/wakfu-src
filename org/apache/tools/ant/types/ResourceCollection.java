package org.apache.tools.ant.types;

import java.util.*;

public interface ResourceCollection extends Iterable<Resource>
{
    Iterator<Resource> iterator();
    
    int size();
    
    boolean isFilesystemOnly();
}
