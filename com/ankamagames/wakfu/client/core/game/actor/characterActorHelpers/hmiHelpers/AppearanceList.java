package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class AppearanceList extends List<Data>
{
    private static final Logger m_logger;
    
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
        final CharacterInfo characterInfo = actor.getCharacterInfo();
        final Data lastData = this.getLast();
        characterInfo.getActor().setAnimationSuffix(removed.m_previousAnimationSuffix);
        if (lastData == null) {
            characterInfo.setForcedGfxId(0);
        }
        else {
            characterInfo.setForcedGfxId(PrimitiveConverter.getInteger(lastData.m_gfxId));
        }
        characterInfo.refreshDisplayEquipment();
        actor.onAnmLoaded(new Runnable() {
            @Override
            public void run() {
                if (actor.getCharacterInfo().isOnFight()) {
                    actor.getCurrentAttack().startUsageAndNotify(actor);
                }
                actor.forceReloadAnimation();
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)AppearanceList.class);
    }
    
    public static class Data extends AdditionnalData
    {
        public final String m_gfxId;
        private String m_previousAnimationSuffix;
        
        public Data(final WakfuRunningEffect effect, final String gfxId) {
            super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
            this.m_gfxId = gfxId;
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_gfxId = dataToCopy.m_gfxId;
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            this.m_previousAnimationSuffix = actor.getAnimationSuffix();
            this.apply(actor, true);
        }
        
        public void apply(final CharacterActor actor, final boolean refreshDisplay) {
            final CharacterInfo characterInfo = actor.getCharacterInfo();
            characterInfo.setForcedGfxId(Integer.parseInt(this.m_gfxId));
            characterInfo.getActor().setAnimationSuffix(null);
            if (refreshDisplay) {
                characterInfo.refreshDisplayEquipment();
            }
            else {
                actor.setGfx(this.m_gfxId);
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
            if (this.m_gfxId != null) {
                if (this.m_gfxId.equals(data.m_gfxId)) {
                    return true;
                }
            }
            else if (data.m_gfxId == null) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + ((this.m_gfxId != null) ? this.m_gfxId.hashCode() : 0);
            return result;
        }
    }
}
