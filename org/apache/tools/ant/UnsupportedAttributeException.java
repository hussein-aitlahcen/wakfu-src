package org.apache.tools.ant;

public class UnsupportedAttributeException extends BuildException
{
    private static final long serialVersionUID = 1L;
    private final String attribute;
    
    public UnsupportedAttributeException(final String msg, final String attribute) {
        super(msg);
        this.attribute = attribute;
    }
    
    public String getAttribute() {
        return this.attribute;
    }
}
