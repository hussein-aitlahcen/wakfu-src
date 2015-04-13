package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SpellsWithPropertyRangeGain extends SpellsWithPropertyModification
{
    private static final ObjectPool m_staticPool;
    
    public SpellsWithPropertyRangeGain() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SpellsWithPropertyRangeGain newInstance() {
        SpellsWithPropertyRangeGain re;
        try {
            re = (SpellsWithPropertyRangeGain)SpellsWithPropertyRangeGain.m_staticPool.borrowObject();
            re.m_pool = SpellsWithPropertyRangeGain.m_staticPool;
        }
        catch (Exception e) {
            re = new SpellsWithPropertyRangeGain();
            re.m_pool = null;
            re.m_isStatic = false;
            SpellsWithPropertyRangeGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un SpellsWithPropertyRangeGain : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    void addSpellsModification(final SpellPropertyType propertyType) {
        ((BasicCharacterInfo)this.m_target).getSpellCostModification().rangeMaxModification(this.m_value, propertyType);
    }
    
    @Override
    void unapplySpellsModification(final SpellPropertyType propertyType) {
        ((BasicCharacterInfo)this.m_target).getSpellCostModification().rangeMaxModification(-this.m_value, propertyType);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SpellsWithPropertyRangeGain>() {
            @Override
            public SpellsWithPropertyRangeGain makeObject() {
                return new SpellsWithPropertyRangeGain();
            }
        });
    }
}
