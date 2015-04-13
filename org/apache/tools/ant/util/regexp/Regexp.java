package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.*;

public interface Regexp extends RegexpMatcher
{
    public static final int REPLACE_FIRST = 1;
    public static final int REPLACE_ALL = 16;
    
    String substitute(String p0, String p1, int p2) throws BuildException;
}
