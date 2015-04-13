package com.ankamagames.framework.fileFormat.rss;

public class MalformedRSSException extends Exception
{
    public MalformedRSSException() {
        super();
    }
    
    public MalformedRSSException(final String message) {
        super(message);
    }
    
    public MalformedRSSException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MalformedRSSException(final Throwable cause) {
        super(cause);
    }
}
