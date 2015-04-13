package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.room.*;

public interface UIInteractionFrame extends MessageFrame
{
    void setElement(ItemizableInfo p0);
    
    void setOnCancel(Runnable p0);
    
    void setOnValidate(Runnable p0);
    
    RoomContentResult getCellResult();
}
