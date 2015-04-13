package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public final class ChangeAnimList extends List<Data>
{
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
    }
    
    public static class Data extends AdditionnalData
    {
        public final String m_animName;
        
        public Data(final WakfuRunningEffect effect, final String animName) {
            super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
            this.m_animName = animName;
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_animName = dataToCopy.m_animName;
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            if (actor.containsAnimation(this.m_animName)) {
                actor.setAnimation(this.m_animName);
                actor.onAppearanceChangedExternally();
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            final Data data = (Data)o;
            if (this.m_animName != null) {
                if (this.m_animName.equals(data.m_animName)) {
                    return true;
                }
            }
            else if (data.m_animName == null) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + ((this.m_animName != null) ? this.m_animName.hashCode() : 0);
            return result;
        }
    }
}
