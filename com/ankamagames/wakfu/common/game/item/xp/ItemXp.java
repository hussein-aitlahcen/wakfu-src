package com.ankamagames.wakfu.common.game.item.xp;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import com.ankamagames.wakfu.common.game.xp.*;

public interface ItemXp extends RawConvertible<RawItemXp>, LoggableEntity
{
    ItemXpDefinition getDefinition();
    
    long getXp();
    
    void addListener(ItemXpModelListener p0);
    
    void removeListener(ItemXpModelListener p0);
    
    short getLevel();
    
    short getMaxLevel();
    
    float getCurrentPercentage();
    
    XpTable getXpTable();
}
