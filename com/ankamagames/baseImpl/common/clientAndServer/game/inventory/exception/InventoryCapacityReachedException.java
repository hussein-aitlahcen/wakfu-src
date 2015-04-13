package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception;

public class InventoryCapacityReachedException extends InventoryException
{
    public InventoryCapacityReachedException() {
        super();
    }
    
    public InventoryCapacityReachedException(final String message) {
        super(message);
    }
    
    public InventoryCapacityReachedException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public InventoryCapacityReachedException(final Throwable cause) {
        super(cause);
    }
}
