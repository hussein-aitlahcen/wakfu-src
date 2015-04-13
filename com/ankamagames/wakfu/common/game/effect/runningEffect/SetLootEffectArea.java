package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SetLootEffectArea extends SetEffectArea
{
    private static final ObjectPool POOL;
    
    @Override
    protected ObjectPool getPool() {
        return SetLootEffectArea.POOL;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        super.executeOverride(linkedRE, trigger);
        this.initializeArea();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        super.effectiveComputeValue(triggerRE);
        this.m_shouldBeInfinite = true;
    }
    
    private void initializeArea() {
        final int dx = this.m_targetCell.getX() - this.m_caster.getWorldCellX();
        final int dy = this.m_targetCell.getY() - this.m_caster.getWorldCellY();
        Direction8 dir;
        if (dx > 1 || dx < -1 || dy > 1 || dy < -1) {
            dir = Direction8.NORTH_EAST;
        }
        else {
            dir = Direction8.getDirectionFromVector(dx, dy);
        }
        this.m_area.setDirection(dir);
    }
    
    static {
        POOL = new MonitoredPool(new ObjectFactory<SetLootEffectArea>() {
            @Override
            public SetLootEffectArea makeObject() {
                return new SetLootEffectArea();
            }
        });
    }
}
