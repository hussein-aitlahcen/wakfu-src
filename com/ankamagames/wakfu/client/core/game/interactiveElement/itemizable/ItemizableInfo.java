package com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable;

import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public interface ItemizableInfo extends RoomContent
{
    long getOwnerId();
    
    boolean canBeRepacked();
    
    boolean canBeAddedIn(Room p0);
    
    void setBag(PersonalSpace p0);
    
    void setOverHeadable(boolean p0);
    
    void notifyViews();
    
    Collection<ClientInteractiveElementView> getViews();
    
    void release();
    
    DragInfo getDragInfo();
    
    void fromRawSynchronisationData(RawItemizableSynchronisationData p0);
    
    InteractiveElementAction[] getInteractiveUsableActions();
    
    AbstractMRUAction[] getInteractiveMRUActions();
    
    boolean onAction(InteractiveElementAction p0, InteractiveElementUser p1);
}
