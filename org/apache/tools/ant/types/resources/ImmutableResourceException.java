package org.apache.tools.ant.types.resources;

import java.io.*;

public class ImmutableResourceException extends IOException
{
    private static final long serialVersionUID = 1L;
    
    public ImmutableResourceException() {
        super();
    }
    
    public ImmutableResourceException(final String s) {
        super(s);
    }
}
