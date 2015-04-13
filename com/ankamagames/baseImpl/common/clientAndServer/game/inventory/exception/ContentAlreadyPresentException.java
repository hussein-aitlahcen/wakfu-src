package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ContentAlreadyPresentException extends InventoryException
{
    public ContentAlreadyPresentException() {
        super();
    }
    
    public ContentAlreadyPresentException(final String message) {
        super(message);
    }
    
    public ContentAlreadyPresentException(final String message, final InventoryContent newOne, final InventoryContent existingOne) {
        super(message + " (old item : " + existingOne + " refid : " + ((existingOne == null) ? "<null>" : existingOne.getReferenceId()) + " new item : " + newOne + " refid : " + ((newOne == null) ? "<null>" : newOne.getReferenceId()));
    }
    
    public ContentAlreadyPresentException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ContentAlreadyPresentException(final Throwable cause) {
        super(cause);
    }
}
