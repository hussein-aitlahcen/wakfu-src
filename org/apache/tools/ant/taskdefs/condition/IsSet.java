package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.*;

public class IsSet extends ProjectComponent implements Condition
{
    private String property;
    
    public void setProperty(final String p) {
        this.property = p;
    }
    
    public boolean eval() throws BuildException {
        if (this.property == null) {
            throw new BuildException("No property specified for isset condition");
        }
        return this.getProject().getProperty(this.property) != null;
    }
}
