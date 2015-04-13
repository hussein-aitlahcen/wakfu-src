package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.spellsFinder.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SummonDoubleForBellaphone extends SummonDouble
{
    private static final ObjectPool m_staticPool;
    public static final short BELLAPHONE_DOUBLE_BREED_ID = 1549;
    private static final short MAX_DOUBLE_SPELL_COUNT = 3;
    
    @Override
    public SummonDoubleForBellaphone newInstance() {
        SummonDoubleForBellaphone re;
        try {
            re = (SummonDoubleForBellaphone)SummonDoubleForBellaphone.m_staticPool.borrowObject();
            re.m_pool = SummonDoubleForBellaphone.m_staticPool;
        }
        catch (Exception e) {
            re = new SummonDoubleForBellaphone();
            re.m_pool = null;
            re.m_isStatic = false;
            SummonDoubleForBellaphone.m_logger.error((Object)("Erreur lors d'un checkOut sur un SummonDoubleForBellaphone : " + e.getMessage()));
        }
        return re;
    }
    
    @NotNull
    @Override
    protected SummonDoubleParams createDoubleParams() {
        final SummonDoubleParams params = new SummonDoubleParams(BellaphoneDoubleCharacteristics.getDefaultInstance(), HighestSpellsElementalOnlyFinder.INSTANCE);
        params.setBreedId((short)1549);
        params.setMaxSpellsCount((short)3);
        params.setUsingCasterAsModel(false);
        params.setDoubleIndependant(true);
        return params;
    }
    
    @Override
    protected Point3 getCellForSummon() {
        final BasicCharacterInfo summoner = this.getSummoner();
        if (summoner == null) {
            return null;
        }
        return summoner.getPosition();
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
        m_staticPool = new MonitoredPool(new ObjectFactory<SummonDoubleForBellaphone>() {
            @Override
            public SummonDoubleForBellaphone makeObject() {
                return new SummonDoubleForBellaphone();
            }
        });
    }
}
