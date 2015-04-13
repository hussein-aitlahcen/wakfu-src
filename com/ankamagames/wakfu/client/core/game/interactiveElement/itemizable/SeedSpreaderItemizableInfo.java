package com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.rawData.*;

public final class SeedSpreaderItemizableInfo extends BasicItemizableInfo
{
    private short m_seedQuantity;
    
    public SeedSpreaderItemizableInfo(final WakfuClientMapInteractiveElement linkedElement) {
        super(linkedElement);
    }
    
    @Override
    protected void unserializePersistantData(final AbstractRawPersistantData specificData) {
    }
    
    @Override
    public GemType[] getAllowedInRooms() {
        return new GemType[0];
    }
    
    @Override
    public boolean canBeRepacked() {
        return this.m_seedQuantity <= 0;
    }
    
    @Override
    public RoomContentType getContentType() {
        return RoomContentType.DECORATION;
    }
    
    public void setSeedQuantity(final short seedQuantity) {
        this.m_seedQuantity = seedQuantity;
    }
}
