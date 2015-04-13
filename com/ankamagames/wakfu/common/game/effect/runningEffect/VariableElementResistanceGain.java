package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class VariableElementResistanceGain extends AbstractVariableElementCharacModification
{
    private static final ObjectPool m_staticPool;
    
    @Override
    public VariableElementResistanceGain newInstance() {
        VariableElementResistanceGain re;
        try {
            re = (VariableElementResistanceGain)VariableElementResistanceGain.m_staticPool.borrowObject();
            re.m_pool = VariableElementResistanceGain.m_staticPool;
        }
        catch (Exception e) {
            re = new VariableElementResistanceGain();
            re.m_pool = null;
            VariableElementResistanceGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un VariableElementResistanceGain : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected FighterCharacteristicType getModifiedCharacteristic(final Elements element) {
        return element.getResistanceBonusCharacteristic();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<VariableElementResistanceGain>() {
            @Override
            public VariableElementResistanceGain makeObject() {
                return new VariableElementResistanceGain();
            }
        });
    }
}
