package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.spellsFinder.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SummonSramDouble extends SummonDouble
{
    private static final ObjectPool m_staticPool;
    public static final short SRAM_DOUBLE_BREED_ID = 1550;
    private static final short MAX_DOUBLE_SPELL_COUNT = 20;
    private static final short DOUBLE_SUPPORT_SPELLS_COUNT = 5;
    
    @Override
    public SummonSramDouble newInstance() {
        SummonSramDouble re;
        try {
            re = (SummonSramDouble)SummonSramDouble.m_staticPool.borrowObject();
            re.m_pool = SummonSramDouble.m_staticPool;
        }
        catch (Exception e) {
            re = new SummonSramDouble();
            re.m_pool = null;
            re.m_isStatic = false;
            SummonSramDouble.m_logger.error((Object)("Erreur lors d'un checkOut sur un SummonSramDouble : " + e.getMessage()));
        }
        return re;
    }
    
    @NotNull
    @Override
    protected SummonDoubleParams createDoubleParams() {
        final SummonDoubleParams params = new SummonDoubleParams(DoubleInvocationCharacteristics.getDefaultInstance(), AllSpellsFinder.INSTANCE);
        params.setBreedId((short)1550);
        params.setMaxSpellsCount((short)20, (short)(-1), (short)5);
        params.setUsingCasterAsModel(true);
        return params;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SummonSramDouble>() {
            @Override
            public SummonSramDouble makeObject() {
                return new SummonSramDouble();
            }
        });
    }
}
