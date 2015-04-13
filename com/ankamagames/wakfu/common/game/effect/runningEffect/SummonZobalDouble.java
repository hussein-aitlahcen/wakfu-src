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

public final class SummonZobalDouble extends SummonDouble
{
    private static final ObjectPool m_staticPool;
    public static final short ZOBAL_DOUBLE_BREED_ID = 2382;
    private static final short MAX_DOUBLE_SPELL_COUNT = 20;
    private static final short DOUBLE_SUPPORT_SPELLS_COUNT = 5;
    
    @Override
    public SummonZobalDouble newInstance() {
        SummonZobalDouble re;
        try {
            re = (SummonZobalDouble)SummonZobalDouble.m_staticPool.borrowObject();
            re.m_pool = SummonZobalDouble.m_staticPool;
        }
        catch (Exception e) {
            re = new SummonZobalDouble();
            re.m_pool = null;
            re.m_isStatic = false;
            SummonZobalDouble.m_logger.error((Object)("Erreur lors d'un checkOut sur un SummonZobalDouble : " + e.getMessage()));
        }
        return re;
    }
    
    @NotNull
    @Override
    protected SummonDoubleParams createDoubleParams() {
        final SummonDoubleParams params = new SummonDoubleParams(DoubleInvocationCharacteristics.getDefaultInstance(), AllSpellsFinder.INSTANCE);
        params.setBreedId((short)2382);
        params.setMaxSpellsCount((short)20, (short)(-1), (short)5);
        params.setUsingCasterAsModel(true);
        params.setShouldUseLevelGains(false);
        return params;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SummonZobalDouble>() {
            @Override
            public SummonZobalDouble makeObject() {
                return new SummonZobalDouble();
            }
        });
    }
}
