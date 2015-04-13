package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception;

public class PositionAlreadyUsedException extends InventoryException
{
    public PositionAlreadyUsedException() {
        super();
    }
    
    public PositionAlreadyUsedException(final String message) {
        super(message);
    }
    
    public PositionAlreadyUsedException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public PositionAlreadyUsedException(final Throwable cause) {
        super(cause);
    }
}
