package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.types.*;
import java.io.*;
import org.apache.tools.ant.*;

public abstract class BaseExtendSelector extends BaseSelector implements ExtendFileSelector
{
    protected Parameter[] parameters;
    
    public BaseExtendSelector() {
        super();
        this.parameters = null;
    }
    
    public void setParameters(final Parameter[] parameters) {
        this.parameters = parameters;
    }
    
    protected Parameter[] getParameters() {
        return this.parameters;
    }
    
    public abstract boolean isSelected(final File p0, final String p1, final File p2) throws BuildException;
}
