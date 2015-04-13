package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SetBombEffectArea extends SetEffectArea
{
    private static final ObjectPool POOL;
    
    @Override
    protected ObjectPool getPool() {
        return SetBombEffectArea.POOL;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        super.executeOverride(linkedRE, trigger);
        this.initializeCooldown();
    }
    
    private void initializeCooldown() {
        if (this.m_caster == null) {
            return;
        }
        if (this.m_area == null) {
            return;
        }
        if (this.m_caster.hasCharacteristic(FighterCharacteristicType.BOMB_COOLDOWN)) {
            final AbstractCharacteristic areaCooldown = this.m_area.getCharacteristic(FighterCharacteristicType.BOMB_COOLDOWN);
            areaCooldown.updateMaxValue(this.m_caster.getCharacteristicValue(FighterCharacteristicType.BOMB_COOLDOWN));
            areaCooldown.toMax();
            ((AbstractBombEffectArea)this.m_area).onCooldownInitialize();
        }
    }
    
    static {
        POOL = new MonitoredPool(new ObjectFactory<SetBombEffectArea>() {
            @Override
            public SetBombEffectArea makeObject() {
                return new SetBombEffectArea();
            }
        });
    }
}
