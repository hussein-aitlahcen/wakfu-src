package com.ankamagames.wakfu.common.game.pet.exception;

public class PetException extends RuntimeException
{
    public PetException(final String msg) {
        super(msg);
    }
    
    public PetException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
