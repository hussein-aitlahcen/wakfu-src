package org.apache.tools.ant;

public class UnsupportedElementException extends BuildException
{
    private static final long serialVersionUID = 1L;
    private final String element;
    
    public UnsupportedElementException(final String msg, final String element) {
        super(msg);
        this.element = element;
    }
    
    public String getElement() {
        return this.element;
    }
}
