package org.json;

public class JSONException extends Exception
{
    private static final long serialVersionUID = 0L;
    private Throwable cause;
    
    public JSONException(final String message) {
        super(message);
    }
    
    public JSONException(final Throwable cause) {
        super(cause.getMessage());
        this.cause = cause;
    }
    
    public Throwable getCause() {
        return this.cause;
    }
}
