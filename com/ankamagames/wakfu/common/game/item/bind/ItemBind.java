package com.ankamagames.wakfu.common.game.item.bind;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public interface ItemBind
{
    ItemBindType getType();
    
    long getData();
    
    void toRaw(RawItemBind p0);
    
    void fromRaw(RawItemBind p0);
    
    ItemBind getCopy();
    
    boolean equals(ItemBind p0);
}
