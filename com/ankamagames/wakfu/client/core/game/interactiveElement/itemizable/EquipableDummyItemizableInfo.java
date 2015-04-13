package com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.rawData.*;

public final class EquipableDummyItemizableInfo extends BasicItemizableInfo<EquipableDummy>
{
    public EquipableDummyItemizableInfo(final EquipableDummy linkedElement) {
        super(linkedElement);
    }
    
    @Override
    protected void unserializePersistantData(final AbstractRawPersistantData specificData) {
        if (specificData.getVirtualId() == 2) {
            final RawPersistantEquipableDummy data = (RawPersistantEquipableDummy)specificData;
            final int previousPackId = ((EquipableDummy)this.m_linkedElement).getItemAttachedRefId();
            ((EquipableDummy)this.m_linkedElement).setItemAttachedRefId(data.content.setPackId);
            ((EquipableDummy)this.m_linkedElement).createItem(previousPackId, ((EquipableDummy)this.m_linkedElement).getItemAttachedRefId(), data.content.item);
        }
    }
    
    @Override
    public boolean canBeRepacked() {
        return ((EquipableDummy)this.m_linkedElement).getItemAttachedRefId() == -1;
    }
    
    @Override
    public GemType[] getAllowedInRooms() {
        return new GemType[] { GemType.GEM_ID_DECORATION, GemType.GEM_ID_CRAFT, GemType.GEM_ID_MERCHANT, GemType.GEM_ID_RESOURCES };
    }
    
    @Override
    public RoomContentType getContentType() {
        return RoomContentType.DECORATION;
    }
}
