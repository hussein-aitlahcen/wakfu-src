package com.ankamagames.wakfu.common.game.havenWorld.exception;

import com.ankamagames.wakfu.common.game.havenWorld.action.*;

public class HavenWorldException extends RuntimeException
{
    private final HavenWorldError m_error;
    
    public HavenWorldException(final HavenWorldError error, final String message) {
        super(error + " : " + message);
        this.m_error = error;
    }
    
    public HavenWorldException(final HavenWorldError error, final String message, final Throwable cause) {
        super(error + " : " + message, cause);
        this.m_error = error;
    }
    
    public HavenWorldError getError() {
        return this.m_error;
    }
}
