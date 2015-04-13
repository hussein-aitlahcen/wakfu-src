package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class PickUpItem extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return PickUpItem.PARAMETERS_LIST_SET;
    }
    
    @Override
    public PickUpItem newInstance() {
        PickUpItem re;
        try {
            re = (PickUpItem)PickUpItem.m_staticPool.borrowObject();
            re.m_pool = PickUpItem.m_staticPool;
        }
        catch (Exception e) {
            re = new PickUpItem();
            re.m_pool = null;
            re.m_isStatic = false;
            PickUpItem.m_logger.error((Object)("Erreur lors d'un newInstance sur PickUpItem : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.isValueComputationEnabled() && this.m_value > 0 && DiceRoll.roll(100) <= this.m_value && this.getCaster() instanceof BasicCharacterInfo && this.getTarget() instanceof BasicCharacterInfo) {
            final BasicCharacterInfo caster = (BasicCharacterInfo)this.getCaster();
            if (this.m_context instanceof WakfuFightEffectContext) {
                final AbstractFight<BasicCharacterInfo> fight = ((WakfuFightEffectContext)this.m_context).getFight();
                if (fight != null) {
                    fight.tryToPickUpLoot(this.m_targetCell.getX(), this.m_targetCell.getY(), caster, true);
                }
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
        this.m_value = 0;
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<PickUpItem>() {
            @Override
            public PickUpItem makeObject() {
                return new PickUpItem();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("chance de loot fixe (%)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de ramasser l'item", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
