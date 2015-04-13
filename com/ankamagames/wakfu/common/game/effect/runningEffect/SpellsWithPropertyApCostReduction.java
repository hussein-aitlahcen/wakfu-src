package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SpellsWithPropertyApCostReduction extends SpellsWithPropertyModification
{
    private static final ObjectPool m_staticPool;
    
    public SpellsWithPropertyApCostReduction() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SpellsWithPropertyApCostReduction newInstance() {
        SpellsWithPropertyApCostReduction re;
        try {
            re = (SpellsWithPropertyApCostReduction)SpellsWithPropertyApCostReduction.m_staticPool.borrowObject();
            re.m_pool = SpellsWithPropertyApCostReduction.m_staticPool;
        }
        catch (Exception e) {
            re = new SpellsWithPropertyApCostReduction();
            re.m_pool = null;
            re.m_isStatic = false;
            SpellsWithPropertyApCostReduction.m_logger.error((Object)("Erreur lors d'un checkOut sur un SpellsWithPropertyApCostReduction : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    void addSpellsModification(final SpellPropertyType propertyType) {
        ((BasicCharacterInfo)this.m_target).getSpellCostModification().apCostModification(-this.m_value, propertyType);
    }
    
    @Override
    void unapplySpellsModification(final SpellPropertyType propertyType) {
        ((BasicCharacterInfo)this.m_target).getSpellCostModification().apCostModification(this.m_value, propertyType);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SpellsWithPropertyApCostReduction>() {
            @Override
            public SpellsWithPropertyApCostReduction makeObject() {
                return new SpellsWithPropertyApCostReduction();
            }
        });
    }
}
