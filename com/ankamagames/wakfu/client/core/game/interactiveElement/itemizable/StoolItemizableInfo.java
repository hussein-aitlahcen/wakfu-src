package com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.rawData.*;

public final class StoolItemizableInfo extends BasicItemizableInfo<Stool>
{
    public StoolItemizableInfo(final Stool linkedElement) {
        super(linkedElement);
    }
    
    @Override
    public boolean canBeRepacked() {
        return true;
    }
    
    @Override
    public GemType[] getAllowedInRooms() {
        return GemType.values();
    }
    
    @Override
    public RoomContentType getContentType() {
        return RoomContentType.DECORATION;
    }
    
    public void unserializePersistantData(final AbstractRawPersistantData data) {
        if (data.getVirtualId() == 6) {
            return;
        }
    }
}
