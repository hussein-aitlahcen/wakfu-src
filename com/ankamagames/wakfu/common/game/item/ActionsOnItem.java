package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.framework.external.*;

public enum ActionsOnItem implements ExportableEnum
{
    USE, 
    USE_IN_FIGHT, 
    EQUIP, 
    DROP, 
    EXCHANGE, 
    CRAFT, 
    PICK_UP, 
    DELETE;
    
    @Override
    public String getEnumId() {
        return this.toString();
    }
    
    @Override
    public String getEnumLabel() {
        return "ON_" + this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
