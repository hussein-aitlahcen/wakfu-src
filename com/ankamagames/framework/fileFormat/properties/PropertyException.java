package com.ankamagames.framework.fileFormat.properties;

public class PropertyException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public PropertyException() {
        super();
    }
    
    public PropertyException(final String arg0) {
        super(arg0);
    }
    
    public PropertyException(final Throwable arg0) {
        super(arg0);
    }
    
    public PropertyException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }
}
