package com.ankamagames.wakfu.common.game.item.xp;

public class ItemXpException extends RuntimeException
{
    public ItemXpException(final String msg) {
        super(msg);
    }
    
    public ItemXpException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
