package com.ankamagames.wakfu.common.game.item.bind;

public class ItemBindException extends RuntimeException
{
    public ItemBindException(final String msg) {
        super(msg);
    }
    
    public ItemBindException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
