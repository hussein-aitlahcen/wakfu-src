package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.*;
import org.apache.tools.ant.util.*;

public class RegexpMatcherFactory
{
    public RegexpMatcher newRegexpMatcher() throws BuildException {
        return this.newRegexpMatcher(null);
    }
    
    public RegexpMatcher newRegexpMatcher(final Project p) throws BuildException {
        String systemDefault = null;
        if (p == null) {
            systemDefault = System.getProperty("ant.regexp.regexpimpl");
        }
        else {
            systemDefault = p.getProperty("ant.regexp.regexpimpl");
        }
        if (systemDefault != null) {
            return this.createInstance(systemDefault);
        }
        return new Jdk14RegexpMatcher();
    }
    
    protected RegexpMatcher createInstance(final String className) throws BuildException {
        return (RegexpMatcher)ClasspathUtils.newInstance(className, RegexpMatcherFactory.class.getClassLoader(), RegexpMatcher.class);
    }
    
    protected void testAvailability(final String className) throws BuildException {
        try {
            Class.forName(className);
        }
        catch (Throwable t) {
            throw new BuildException(t);
        }
    }
    
    public static boolean regexpMatcherPresent(final Project project) {
        try {
            new RegexpMatcherFactory().newRegexpMatcher(project);
            return true;
        }
        catch (Throwable ex) {
            return false;
        }
    }
}
