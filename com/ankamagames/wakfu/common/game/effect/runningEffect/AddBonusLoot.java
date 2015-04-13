package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class AddBonusLoot extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private short m_quantity;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AddBonusLoot.PARAMETERS_LIST_SET;
    }
    
    public AddBonusLoot() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public AddBonusLoot newInstance() {
        AddBonusLoot re;
        try {
            re = (AddBonusLoot)AddBonusLoot.m_staticPool.borrowObject();
            re.m_pool = AddBonusLoot.m_staticPool;
        }
        catch (Exception e) {
            re = new AddBonusLoot();
            re.m_pool = null;
            re.m_isStatic = false;
            AddBonusLoot.m_logger.error((Object)("Erreur lors d'un checkOut sur un AddBonusLoot : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final short level = this.getContainerLevel();
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
            this.m_quantity = (short)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        else if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 3) {
            final float a = ((WakfuEffect)this.m_genericEffect).getParam(1, level);
            final int b = ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_quantity = (short)StrictMath.pow(a, level / b);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null || this.m_genericEffect == null) {
            this.setNotified();
            return;
        }
        if (!this.isValueComputationEnabled()) {
            return;
        }
        final AbstractReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(this.m_value);
        if (item == null) {
            AddBonusLoot.m_logger.error((Object)("L'item a ajouter au loot n'existe pas " + this.m_value));
            this.setNotified();
            return;
        }
        if (!(this.m_target instanceof Looter) || !(this.m_target instanceof BasicFighter)) {
            this.setNotified();
            return;
        }
        final AbstractFight currentFight = (AbstractFight)((BasicFighter)this.m_target).getCurrentFight();
        currentFight.addBonusLoot((Looter)this.m_target, this.m_value, this.m_quantity);
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<AddBonusLoot>() {
            @Override
            public AddBonusLoot makeObject() {
                return new AddBonusLoot();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Loot refId", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Quantit\u00e9", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Loot refId", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Constante A (float) pour Quantit\u00e9 A^(lvl/B) ", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Constante B (int) pour Quantit\u00e9 A^(lvl/B) ", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
