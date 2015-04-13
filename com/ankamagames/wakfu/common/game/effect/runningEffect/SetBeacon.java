package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SetBeacon extends SetEffectArea
{
    private static final ObjectPool m_beaconEffectAreaPool;
    
    @Override
    protected ObjectPool getPool() {
        return SetBeacon.m_beaconEffectAreaPool;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        super.executeOverride(linkedRE, trigger);
        if (((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 11) {
            final AbstractSpellLevel spellContainer = (AbstractSpellLevel)this.m_effectContainer;
            ((AbstractBeaconEffectArea)this.m_area).setLinkedSpellId(spellContainer.getReferenceId());
        }
    }
    
    static {
        m_beaconEffectAreaPool = new MonitoredPool(new ObjectFactory<SetBeacon>() {
            @Override
            public SetBeacon makeObject() {
                return new SetBeacon();
            }
        });
    }
}
