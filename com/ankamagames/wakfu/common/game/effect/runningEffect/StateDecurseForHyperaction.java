package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class StateDecurseForHyperaction extends StateDecurse
{
    private boolean m_isCheckedOut;
    private static final ObjectPool POOL;
    
    public static StateDecurseForHyperaction checkOut() {
        StateDecurseForHyperaction obj;
        try {
            obj = (StateDecurseForHyperaction)StateDecurseForHyperaction.POOL.borrowObject();
            obj.m_isCheckedOut = true;
        }
        catch (Exception e) {
            obj = new StateDecurseForHyperaction();
            StateDecurseForHyperaction.m_logger.error((Object)("Erreur lors d'un checkOut sur un objet de type StateDecurseForHyperaction : " + e.getMessage()));
        }
        return obj;
    }
    
    @Override
    public void release() {
        if (this.m_isCheckedOut) {
            try {
                StateDecurseForHyperaction.POOL.returnObject(this);
                this.m_isCheckedOut = false;
            }
            catch (Exception e) {
                StateDecurseForHyperaction.m_logger.error((Object)"Exception lors du retour au pool", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public StateDecurseForHyperaction newInstance() {
        StateDecurseForHyperaction re;
        try {
            re = (StateDecurseForHyperaction)StateDecurseForHyperaction.POOL.borrowObject();
            re.m_pool = StateDecurseForHyperaction.POOL;
        }
        catch (Exception e) {
            re = new StateDecurseForHyperaction();
            re.m_pool = null;
            re.m_isStatic = false;
            StateDecurseForHyperaction.m_logger.error((Object)("Erreur lors d'un checkOut sur un StateDecurse : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        super.effectiveComputeValue(triggerRE);
        if (this.m_target == null) {
            return;
        }
        this.m_levelToDecrease *= this.m_target.getCharacteristicValue(FighterCharacteristicType.AP);
    }
    
    static {
        POOL = new MonitoredPool(new ObjectFactory<StateDecurseForHyperaction>() {
            @Override
            public StateDecurseForHyperaction makeObject() {
                return new StateDecurseForHyperaction();
            }
        });
    }
}
