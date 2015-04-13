package org.apache.tools.ant.filters;

import org.apache.tools.ant.types.*;
import java.io.*;

public abstract class BaseParamFilterReader extends BaseFilterReader implements Parameterizable
{
    private Parameter[] parameters;
    
    public BaseParamFilterReader() {
        super();
    }
    
    public BaseParamFilterReader(final Reader in) {
        super(in);
    }
    
    public final void setParameters(final Parameter[] parameters) {
        this.parameters = parameters;
        this.setInitialized(false);
    }
    
    protected final Parameter[] getParameters() {
        return this.parameters;
    }
}
