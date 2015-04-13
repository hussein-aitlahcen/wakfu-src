package com.ankamagames.wakfu.common.game.havenWorld.exception;

public class HavenWorldRuntimeException extends RuntimeException
{
    public HavenWorldRuntimeException(final String message) {
        super(message);
    }
    
    public HavenWorldRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
