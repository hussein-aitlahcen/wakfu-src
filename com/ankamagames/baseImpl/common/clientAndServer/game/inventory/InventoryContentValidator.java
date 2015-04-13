package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

public interface InventoryContentValidator<Content extends InventoryContent>
{
    boolean isValid(Content p0);
}
