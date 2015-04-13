package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.*;
import java.util.*;

public interface RegexpMatcher
{
    public static final int MATCH_DEFAULT = 0;
    public static final int MATCH_CASE_INSENSITIVE = 256;
    public static final int MATCH_MULTILINE = 4096;
    public static final int MATCH_SINGLELINE = 65536;
    
    void setPattern(String p0) throws BuildException;
    
    String getPattern() throws BuildException;
    
    boolean matches(String p0) throws BuildException;
    
    Vector getGroups(String p0) throws BuildException;
    
    boolean matches(String p0, int p1) throws BuildException;
    
    Vector getGroups(String p0, int p1) throws BuildException;
}
