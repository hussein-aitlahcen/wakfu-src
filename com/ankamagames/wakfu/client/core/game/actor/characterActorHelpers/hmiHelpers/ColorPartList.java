package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class ColorPartList extends List<Data>
{
    private static final Logger m_logger;
    
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
        actor.getCharacterInfo().refreshDisplayEquipment();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ColorPartList.class);
    }
    
    public static class Data extends AdditionnalData
    {
        public final float[] m_color;
        public final int m_index;
        
        public Data(final WakfuRunningEffect effect, final float[] color, final int index) {
            super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
            this.m_color = getCustomColor(color);
            this.m_index = index;
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_index = dataToCopy.m_index;
            this.m_color = dataToCopy.m_color;
        }
        
        private static float[] getCustomColor(final float[] color) {
            return new float[] { color[0] * 1.25f, color[1] * 1.25f, color[2] * 1.25f, 1.0f };
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            this.apply(actor, true);
        }
        
        public void apply(final CharacterActor actor, final boolean refreshDisplay) {
            actor.setCustomColor(this.m_index, this.m_color);
            if (refreshDisplay) {
                actor.getCharacterInfo().refreshDisplayEquipment();
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
            return this.m_index == data.m_index && Arrays.equals(this.m_color, data.m_color);
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + ((this.m_color != null) ? Arrays.hashCode(this.m_color) : 0);
            result = 31 * result + this.m_index;
            return result;
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
    }
}
