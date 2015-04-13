package org.apache.tools.ant;

public class ExitStatusException extends BuildException
{
    private static final long serialVersionUID = 7760846806886585968L;
    private int status;
    
    public ExitStatusException(final int status) {
        super();
        this.status = status;
    }
    
    public ExitStatusException(final String msg, final int status) {
        super(msg);
        this.status = status;
    }
    
    public ExitStatusException(final String message, final int status, final Location location) {
        super(message, location);
        this.status = status;
    }
    
    public int getStatus() {
        return this.status;
    }
}
