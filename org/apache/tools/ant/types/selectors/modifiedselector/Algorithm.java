package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.*;

public interface Algorithm
{
    boolean isValid();
    
    String getValue(File p0);
}
