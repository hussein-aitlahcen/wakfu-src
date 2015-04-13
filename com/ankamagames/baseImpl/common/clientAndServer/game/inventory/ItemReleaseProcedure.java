package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import gnu.trove.*;

class ItemReleaseProcedure implements TObjectProcedure<InventoryContent>
{
    static final ItemReleaseProcedure INSTANCE;
    
    @Override
    public boolean execute(final InventoryContent object) {
        object.release();
        return true;
    }
    
    static {
        INSTANCE = new ItemReleaseProcedure();
    }
}
