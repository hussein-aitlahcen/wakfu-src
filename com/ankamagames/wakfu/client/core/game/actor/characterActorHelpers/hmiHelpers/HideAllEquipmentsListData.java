package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class HideAllEquipmentsListData extends List.AdditionnalData
{
    public HideAllEquipmentsListData(final WakfuRunningEffect effect) {
        super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
    }
    
    @Override
    public HideAllEquipmentsListData duplicateForNewList() {
        return new HideAllEquipmentsListData(this);
    }
    
    HideAllEquipmentsListData(final HideAllEquipmentsListData dataToCopy) {
        super(dataToCopy.m_effect);
    }
    
    @Override
    public void apply(final CharacterActor actor) {
        this.apply(actor, true);
    }
    
    public void apply(final CharacterActor actor, final boolean refreshAppearance) {
        actor.setHideAllEquipments(true);
        if (refreshAppearance) {
            actor.getCharacterInfo().refreshDisplayEquipment();
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && super.equals(o));
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
