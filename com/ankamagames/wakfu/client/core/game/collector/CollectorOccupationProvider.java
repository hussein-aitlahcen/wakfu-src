package com.ankamagames.wakfu.client.core.game.collector;

import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.collector.*;

public interface CollectorOccupationProvider
{
    void unSerializeInventory(byte[] p0);
    
    IECollectorParameter getInfo();
    
    WakfuClientMapInteractiveElement getInteractiveElement();
    
    CollectorInventory getInventory();
}
