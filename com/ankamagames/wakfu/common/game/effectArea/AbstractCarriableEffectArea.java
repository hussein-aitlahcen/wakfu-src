package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public abstract class AbstractCarriableEffectArea extends AbstractEffectArea implements CarryTarget, MovementEffectUser
{
    private CarryTargetInfo m_carryInfo;
    
    AbstractCarriableEffectArea() {
        super();
        this.m_carryInfo = new CarryTargetInfo(this);
    }
    
    AbstractCarriableEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_carryInfo = new CarryTargetInfo(this);
    }
    
    @Override
    public boolean canJumpFromCarrier() {
        return this.m_carryInfo.canJumpFromCarrier();
    }
    
    @Override
    public boolean canBeCarriedBy(final Carrier carrier) {
        return this.m_carryInfo.canBeCarriedBy(carrier);
    }
    
    @Override
    public boolean isCarried() {
        return this.m_carryInfo.isCarried();
    }
    
    @Override
    public void onCarryEvent(final Carrier carrier) {
        this.m_carryInfo.onCarryEvent(carrier);
    }
    
    @Override
    public void onUncarryEvent() {
        this.m_carryInfo.onUncarryEvent();
    }
    
    @Override
    public Carrier getCarrier() {
        return this.m_carryInfo.getCarrier();
    }
    
    @Override
    public short getJumpCapacity() {
        return 1;
    }
    
    @Override
    public void teleport(final int x, final int y, final short z) {
        this.setPosition(x, y, z);
        this.onPositionChanged();
    }
    
    @Override
    public void onPositionChanged() {
        if (this.mustGoOffPlay() || this.mustGoOutOfPlay()) {
            return;
        }
        this.computeZone();
        final FightMap map = this.getContext().getFightMap();
        if (map != null && this.canBlockMovementOrSight() && !this.isCarried()) {
            map.moveObstacle(this, this.getWorldCellX(), this.getWorldCellY());
        }
    }
}
