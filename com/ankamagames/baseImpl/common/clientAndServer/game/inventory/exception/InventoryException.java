package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception;

public class InventoryException extends Exception
{
    public InventoryException() {
        super();
    }
    
    public InventoryException(final String message) {
        super(message);
    }
    
    public InventoryException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public InventoryException(final Throwable cause) {
        super(cause);
    }
}
