package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.account.subscription.*;

public final class HpLossFromLootArea extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_ratio;
    private boolean m_improvedDrop;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossFromLootArea.PARAMETERS_LIST_SET;
    }
    
    public HpLossFromLootArea() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HpLossFromLootArea newInstance() {
        HpLossFromLootArea re;
        try {
            re = (HpLossFromLootArea)HpLossFromLootArea.m_staticPool.borrowObject();
            re.m_pool = HpLossFromLootArea.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossFromLootArea();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossFromLootArea.m_logger.error((Object)("Erreur lors d'un checkOut sur un DropFromLootArea : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 1) {
            return;
        }
        this.m_ratio = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 2) {
            this.m_improvedDrop = false;
            return;
        }
        this.m_improvedDrop = (((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        this.setNotified();
        AbstractLootEffectArea lootArea = null;
        if (this.m_effectContainer instanceof AbstractLootEffectArea) {
            lootArea = (AbstractLootEffectArea)this.m_effectContainer;
        }
        if (!(this.m_context instanceof WakfuFightEffectContext)) {
            this.setNotified();
            return;
        }
        final AbstractFight fight = ((WakfuFightEffectContext)this.m_context).getFight();
        if (lootArea == null) {
            final Collection<BasicEffectArea> areas = fight.getEffectAreaManager().getActiveEffectAreas();
            for (final BasicEffectArea area : areas) {
                if (area.getType() == EffectAreaType.LOOT_AREA.getTypeId() && area.contains(this.m_targetCell)) {
                    lootArea = (AbstractLootEffectArea)area;
                    break;
                }
            }
        }
        final Looter looter = (this.m_caster instanceof Looter) ? this.m_caster : new FakeLooter();
        this.m_value = fight.dropEnutrofPurse((Dropper)lootArea.getOwner(), looter, false, this.m_improvedDrop);
        if (this.m_value == 0) {
            return;
        }
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_value);
        final short itemLevel = referenceItem.getLevel();
        final int hpLossValue = this.m_ratio * itemLevel;
        final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, Elements.EARTH, HPLoss.ComputeMode.CLASSIC, hpLossValue, null);
        hpLoss.disableValueComputation();
        ((RunningEffect<WakfuEffect, EC>)hpLoss).setGenericEffect(((RunningEffect<WakfuEffect, EC>)this).getGenericEffect());
        hpLoss.setCaster(this.m_caster);
        final EffectExecutionResult effectExecutionResult = hpLoss.run(((RunningEffect<WakfuEffect, EC>)this).getGenericEffect(), (WakfuEffectContainer)this.m_effectContainer, this.m_context, this.m_caster, this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), null, this.getParams());
        if (effectExecutionResult != null) {
            effectExecutionResult.release();
        }
        hpLoss.release();
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
        this.m_ratio = 0;
        this.m_improvedDrop = false;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossFromLootArea>() {
            @Override
            public HpLossFromLootArea makeObject() {
                return new HpLossFromLootArea();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Ratio dgts/lvl", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Ratio degats/level", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Drop am\u00e9liorer", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Ratio degats/level", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Drop Am\u00e9lior\u00e9 (1 = oui, 0 = non (defaut)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
    
    private static class FakeLooter implements Looter
    {
        @Override
        public long getId() {
            return -1L;
        }
        
        @Override
        public Object getCriterionContext() {
            return null;
        }
        
        @Override
        public int getProspection() {
            return 0;
        }
        
        @Override
        public short getInstanceId() {
            return 0;
        }
        
        @Override
        public int getBonusTries() {
            return 0;
        }
        
        @Override
        public boolean alwaysLoot() {
            return false;
        }
        
        @Override
        public Looter getLootReceiver() {
            return this;
        }
        
        @Override
        public float getLootRatio() {
            return 1.0f;
        }
        
        @Override
        public SubscriptionLevel getSubscriptionLevel() {
            return SubscriptionLevel.UNKNOWN;
        }
    }
}
