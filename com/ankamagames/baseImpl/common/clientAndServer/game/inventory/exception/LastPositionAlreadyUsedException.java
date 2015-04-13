package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception;

public class LastPositionAlreadyUsedException extends InventoryException
{
    public LastPositionAlreadyUsedException() {
        super();
    }
    
    public LastPositionAlreadyUsedException(final String message) {
        super(message);
    }
    
    public LastPositionAlreadyUsedException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public LastPositionAlreadyUsedException(final Throwable cause) {
        super(cause);
    }
}
