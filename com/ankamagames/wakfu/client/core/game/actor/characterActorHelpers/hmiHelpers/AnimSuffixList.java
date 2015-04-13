package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class AnimSuffixList extends List<Data>
{
    private static final Logger m_logger;
    private String m_defaultValue;
    
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
        if (this.isEmpty()) {
            assert this.m_defaultValue == null;
            this.m_defaultValue = actor.getAnimationSuffix();
        }
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
        if (this.isEmpty()) {
            actor.setAnimationSuffix(this.m_defaultValue);
            this.m_defaultValue = null;
        }
        else {
            final Data next = this.getLast();
            if (!next.equals(current)) {
                next.apply(actor);
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnimSuffixList.class);
    }
    
    public static class Data extends AdditionnalData
    {
        public final String m_animSuffix;
        
        public Data(final WakfuRunningEffect effect, final String animSuffix) {
            super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
            this.m_animSuffix = animSuffix;
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_animSuffix = dataToCopy.m_animSuffix;
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            actor.setAnimationSuffix(this.m_animSuffix);
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
            if (this.m_animSuffix != null) {
                if (this.m_animSuffix.equals(data.m_animSuffix)) {
                    return true;
                }
            }
            else if (data.m_animSuffix == null) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + ((this.m_animSuffix != null) ? this.m_animSuffix.hashCode() : 0);
            return result;
        }
    }
}
