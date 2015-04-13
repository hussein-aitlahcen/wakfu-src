package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import com.google.common.collect.*;
import com.google.common.base.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import javax.annotation.*;

public class CostumeList extends List<CostumeListData>
{
    public CostumeListData getVisibleCostume() {
        return (CostumeListData)Ordering.natural().onResultOf((Function)Weight.INSTANCE).max((Iterable)this.m_stack);
    }
    
    @Override
    protected void onAdding(final CharacterActor actor, final CostumeListData data) {
    }
    
    @Override
    public void onRemoved(final CostumeListData current, final CostumeListData removed, final CharacterActor actor) {
        actor.getCharacterInfo().refreshDisplayEquipment();
    }
    
    protected static class Weight implements Function<CostumeListData, Integer>
    {
        protected static final Weight INSTANCE;
        
        @Nullable
        public Integer apply(@Nullable final CostumeListData input) {
            return (input == null) ? 0 : input.getWeight();
        }
        
        static {
            INSTANCE = new Weight();
        }
    }
}
