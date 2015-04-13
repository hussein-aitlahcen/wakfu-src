package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SpawnMonsterForArcadeDungeon extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_breedId;
    private int m_level;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SpawnMonsterForArcadeDungeon.PARAMETERS_LIST_SET;
    }
    
    public SpawnMonsterForArcadeDungeon() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SpawnMonsterForArcadeDungeon newInstance() {
        SpawnMonsterForArcadeDungeon re;
        try {
            re = (SpawnMonsterForArcadeDungeon)SpawnMonsterForArcadeDungeon.m_staticPool.borrowObject();
            re.m_pool = SpawnMonsterForArcadeDungeon.m_staticPool;
        }
        catch (Exception e) {
            re = new SpawnMonsterForArcadeDungeon();
            re.m_pool = null;
            re.m_isStatic = false;
            SpawnMonsterForArcadeDungeon.m_logger.error((Object)("Erreur lors d'un checkOut sur un SpawnMonster : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_breedId = -1;
        this.m_level = -1;
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 2) {
            return;
        }
        this.m_breedId = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_level = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_breedId == -1) {
            return;
        }
        final MonsterSpawner monsterSpawner = this.getContext().getMonsterSpawner();
        if (monsterSpawner == null) {
            return;
        }
        final Collection<WorldPropertyType> property = Collections.singleton(WorldPropertyType.IS_ARCADE_NPC);
        monsterSpawner.spawnNpcAndAddItToFight(this.m_breedId, this.m_level, this.m_targetCell, property);
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    @Override
    public void onCheckIn() {
        this.m_breedId = -1;
        this.m_level = -1;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SpawnMonsterForArcadeDungeon>() {
            @Override
            public SpawnMonsterForArcadeDungeon makeObject() {
                return new SpawnMonsterForArcadeDungeon();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Breed Id et Level", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Breed Id", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
