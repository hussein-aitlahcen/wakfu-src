package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class VariableElementDamageGain extends AbstractVariableElementCharacModification
{
    private static final ObjectPool m_staticPool;
    
    @Override
    public VariableElementDamageGain newInstance() {
        VariableElementDamageGain re;
        try {
            re = (VariableElementDamageGain)VariableElementDamageGain.m_staticPool.borrowObject();
            re.m_pool = VariableElementDamageGain.m_staticPool;
        }
        catch (Exception e) {
            re = new VariableElementDamageGain();
            re.m_pool = null;
            VariableElementDamageGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un VariableElementDamageGain : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected FighterCharacteristicType getModifiedCharacteristic(final Elements element) {
        return element.getDamageBonusCharacteristic();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<VariableElementDamageGain>() {
            @Override
            public VariableElementDamageGain makeObject() {
                return new VariableElementDamageGain();
            }
        });
    }
}
