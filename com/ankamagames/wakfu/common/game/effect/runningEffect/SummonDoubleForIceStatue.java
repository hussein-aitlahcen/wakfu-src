package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.spellsFinder.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SummonDoubleForIceStatue extends SummonDouble
{
    private static final ObjectPool m_staticPool;
    private static final short BREED_ID = 2349;
    private static final short MAX_DOUBLE_SPELL_COUNT = 4;
    
    @Override
    public SummonDoubleForIceStatue newInstance() {
        SummonDoubleForIceStatue re;
        try {
            re = (SummonDoubleForIceStatue)SummonDoubleForIceStatue.m_staticPool.borrowObject();
            re.m_pool = SummonDoubleForIceStatue.m_staticPool;
        }
        catch (Exception e) {
            re = new SummonDoubleForIceStatue();
            re.m_pool = null;
            re.m_isStatic = false;
            SummonDoubleForIceStatue.m_logger.error((Object)("Erreur lors d'un checkOut sur un SummonDoubleForIceStatue : " + e.getMessage()));
        }
        return re;
    }
    
    @NotNull
    @Override
    protected SummonDoubleParams createDoubleParams() {
        final SummonDoubleParams params = new SummonDoubleParams(IceStatueDoubleCharacteristics.getDefaultInstance(), HighestSpellsElementalOnlyFinder.INSTANCE);
        params.setBreedId((short)2349);
        params.setMaxSpellsCount((short)4);
        params.setUsingCasterAsModel(false);
        return params;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SummonDoubleForIceStatue>() {
            @Override
            public SummonDoubleForIceStatue makeObject() {
                return new SummonDoubleForIceStatue();
            }
        });
    }
}
