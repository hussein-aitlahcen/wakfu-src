package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class HideAllEquipmentsList extends List<HideAllEquipmentsListData>
{
    private static final Logger m_logger;
    
    @Override
    protected void onAdding(final CharacterActor actor, final HideAllEquipmentsListData data) {
    }
    
    @Override
    public void onRemoved(final HideAllEquipmentsListData current, final HideAllEquipmentsListData removed, final CharacterActor actor) {
        if (this.isEmpty()) {
            actor.setHideAllEquipments(false);
        }
        else {
            final HideAllEquipmentsListData next = this.getLast();
            if (!next.equals(current)) {
                next.apply(actor);
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)HideAllEquipmentsList.class);
    }
}
