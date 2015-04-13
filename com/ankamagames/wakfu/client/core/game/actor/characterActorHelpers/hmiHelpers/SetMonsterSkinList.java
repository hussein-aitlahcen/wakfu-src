package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class SetMonsterSkinList extends List<Data>
{
    private static final Logger m_logger;
    
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
        if (this.isEmpty()) {
            actor.setMonsterSkin(null);
        }
        else {
            final Data next = this.getLast();
            if (!next.equals(current)) {
                next.apply(actor);
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMonsterSkinList.class);
    }
    
    public static class Data extends AdditionnalData
    {
        public final MonsterSkin m_skin;
        
        public Data(final WakfuRunningEffect effect, final String monsterId, final boolean displayEquipment) {
            super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
            this.m_skin = new MonsterSkin(monsterId, displayEquipment);
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_skin = dataToCopy.m_skin;
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            actor.setMonsterSkin(this.m_skin);
            this.m_skin.applyTo(actor.getCharacterInfo());
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
            return this.m_skin.equals(data.m_skin);
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + this.m_skin.hashCode();
            return result;
        }
    }
}
