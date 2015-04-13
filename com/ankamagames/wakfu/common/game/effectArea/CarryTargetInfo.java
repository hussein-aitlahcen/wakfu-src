package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

final class CarryTargetInfo
{
    private final AbstractCarriableEffectArea m_linkedCarriableArea;
    private Carrier m_carrier;
    
    CarryTargetInfo(final AbstractCarriableEffectArea linkedCarriableArea) {
        super();
        this.m_linkedCarriableArea = linkedCarriableArea;
    }
    
    public boolean canJumpFromCarrier() {
        return false;
    }
    
    public boolean canBeCarriedBy(final Carrier carrier) {
        return carrier.getController() == this.m_linkedCarriableArea.getOwner();
    }
    
    public boolean isCarried() {
        return this.m_carrier != null;
    }
    
    public void onCarryEvent(final Carrier carrier) {
        this.m_carrier = carrier;
        final FightMap map = this.m_linkedCarriableArea.getContext().getFightMap();
        if (map != null && this.m_linkedCarriableArea.canBlockMovementOrSight()) {
            map.modifyObstacle(this.m_linkedCarriableArea, false);
        }
    }
    
    public void onUncarryEvent() {
        this.m_carrier = null;
        this.m_linkedCarriableArea.onPositionChanged();
    }
    
    public Carrier getCarrier() {
        return this.m_carrier;
    }
}
