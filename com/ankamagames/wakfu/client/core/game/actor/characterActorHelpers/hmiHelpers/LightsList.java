package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.light.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class LightsList extends List<Data>
{
    private static final Logger m_logger;
    
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
        if (removed.m_lightId != 0) {
            IsoSceneLightManager.INSTANCE.shutdownLight(removed.m_lightId, 500);
            removed.m_lightId = 0;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)LightsList.class);
    }
    
    public static class Data extends AdditionnalData
    {
        public final float[] m_color;
        public final float m_range;
        public final boolean m_instant;
        int m_lightId;
        
        public Data(final WakfuRunningEffect effect, final float[] color, final float range, final boolean instant) {
            super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
            this.m_color = color;
            this.m_range = range;
            this.m_instant = instant;
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_color = dataToCopy.m_color;
            this.m_range = dataToCopy.m_range;
            this.m_instant = dataToCopy.m_instant;
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            final IsoLightSource lightSpot = (IsoLightSource)LightSourceManagerDelegate.INSTANCE.createLightSource();
            lightSpot.setTarget(actor);
            lightSpot.setBaseColor(0.0f, 0.0f, 0.0f);
            lightSpot.setSaturation(this.m_color[0], this.m_color[1], this.m_color[2]);
            lightSpot.setAttenuation(0.0f, 0.0f, 0.2f);
            lightSpot.setRange(this.m_range);
            IsoSceneLightManager.INSTANCE.addLight(lightSpot);
            if (this.m_instant) {
                lightSpot.shutdown(500);
            }
            else {
                this.m_lightId = lightSpot.getId();
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
            return this.m_instant == data.m_instant && Float.compare(data.m_range, this.m_range) == 0 && Arrays.equals(this.m_color, data.m_color);
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + ((this.m_color != null) ? Arrays.hashCode(this.m_color) : 0);
            result = 31 * result + ((this.m_range != 0.0f) ? Float.floatToIntBits(this.m_range) : 0);
            result = 31 * result + (this.m_instant ? 1 : 0);
            return result;
        }
    }
}
