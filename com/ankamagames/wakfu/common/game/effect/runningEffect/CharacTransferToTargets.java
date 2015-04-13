package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacTransferToTargets extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private CharacteristicType m_charac;
    private RunningEffectDefinition m_buffReferenceEffect;
    private RunningEffectDefinition m_lossReferenceEffect;
    private int m_percentToTransfer;
    private boolean m_rollbackAtUnapplication;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacTransferToTargets.PARAMETERS_LIST_SET;
    }
    
    public CharacTransferToTargets() {
        super();
        this.setTriggersToExecute();
    }
    
    public CharacTransferToTargets(final FighterCharacteristicType charac, final RunningEffectDefinition buffReferenceEffect, final RunningEffectDefinition lossReferenceEffect) {
        super();
        this.m_charac = charac;
        this.m_buffReferenceEffect = buffReferenceEffect;
        this.m_lossReferenceEffect = lossReferenceEffect;
        this.m_rollbackAtUnapplication = false;
    }
    
    public CharacTransferToTargets(final FighterCharacteristicType charac, final RunningEffectDefinition buffReferenceEffect, final RunningEffectDefinition lossReferenceEffect, final boolean rollbackAtUnapplication) {
        super();
        this.m_charac = charac;
        this.m_buffReferenceEffect = buffReferenceEffect;
        this.m_lossReferenceEffect = lossReferenceEffect;
        this.m_rollbackAtUnapplication = rollbackAtUnapplication;
    }
    
    @Override
    public CharacTransferToTargets newInstance() {
        CharacTransferToTargets re;
        try {
            re = (CharacTransferToTargets)CharacTransferToTargets.m_staticPool.borrowObject();
            re.m_pool = CharacTransferToTargets.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacTransferToTargets();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacTransferToTargets.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacTransferToTargets : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        re.m_buffReferenceEffect = this.m_buffReferenceEffect;
        re.m_lossReferenceEffect = this.m_lossReferenceEffect;
        re.m_rollbackAtUnapplication = this.m_rollbackAtUnapplication;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_percentToTransfer = 0;
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 0) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_percentToTransfer = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (this.m_caster == null || !this.m_caster.hasCharacteristic(this.m_charac)) {
            return;
        }
        final List<List<EffectUser>> potentialTargets = this.determineTargets((WakfuEffect)this.m_genericEffect, this.m_caster, (EffectContext<WakfuEffect>)this.m_context, this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ());
        final List<EffectUser> potentialTargetList = new ArrayList<EffectUser>();
        for (int i = 0, n = potentialTargets.size(); i < n; ++i) {
            final List<EffectUser> effectUsers = potentialTargets.get(i);
            for (int j = 0, k = effectUsers.size(); j < k; ++j) {
                final EffectUser effectUser = effectUsers.get(j);
                if (!potentialTargetList.contains(effectUser) && effectUser.hasCharacteristic(this.m_charac)) {
                    potentialTargetList.add(effectUser);
                }
            }
        }
        if (potentialTargetList.isEmpty()) {
            return;
        }
        if (this.isValueComputationEnabled()) {
            this.m_value = this.m_caster.getCharacteristicValue(this.m_charac);
            if (this.m_percentToTransfer > 0) {
                this.m_value = this.m_value * this.m_percentToTransfer / 100;
            }
            final int baseValueToDistribute = this.m_value / potentialTargetList.size();
            final int modValueToDistribute = this.m_value % potentialTargetList.size();
            for (int l = 0, n2 = potentialTargetList.size(); l < n2; ++l) {
                final EffectUser effectUser = potentialTargetList.get(l);
                int charecBuffValue = baseValueToDistribute;
                if (modValueToDistribute > 0 && l == potentialTargetList.size() - 1) {
                    charecBuffValue += modValueToDistribute;
                }
                this.buffCharac(effectUser, charecBuffValue);
            }
        }
        final CharacLoss casterLoss = new CharacLoss(this.m_charac);
        casterLoss.setId(this.m_lossReferenceEffect.getId());
        casterLoss.setTarget(this.m_caster);
        casterLoss.disableValueComputation();
        casterLoss.forceValue(this.m_value);
        casterLoss.setParent(this);
        casterLoss.setCaster(this.m_caster);
        ((RunningEffect<DefaultFightOneTurnEffect, EC>)casterLoss).setGenericEffect(DefaultFightOneTurnEffect.getInstance());
        casterLoss.setContext(this.m_context);
        casterLoss.execute(null, false);
    }
    
    private void buffCharac(final EffectUser effectUser, final int charecBuffValue) {
        final WakfuRunningEffect refRE = this.m_buffReferenceEffect.getObject();
        WakfuRunningEffect characBuff;
        if (refRE instanceof CharacBuff) {
            final CharacBuff buff = (CharacBuff)(characBuff = new CharacBuff(this.m_charac));
            buff.setAddCurrentValue(true);
        }
        else {
            if (!(refRE instanceof CharacGain)) {
                CharacTransferToTargets.m_logger.error((Object)("Type d'effet non g\u00e9r\u00e9 " + refRE));
                return;
            }
            characBuff = new CharacGain(this.m_charac);
        }
        characBuff.setId(this.m_buffReferenceEffect.getId());
        characBuff.setTarget(effectUser);
        characBuff.setTargetCell(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ());
        characBuff.disableValueComputation();
        characBuff.forceValue(charecBuffValue);
        characBuff.setParent(this);
        characBuff.setCaster(this.m_caster);
        ((RunningEffect<WakfuEffect, EC>)characBuff).setGenericEffect((WakfuEffect)this.m_genericEffect);
        characBuff.setContext(this.m_context);
        characBuff.execute(null, false);
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_rollbackAtUnapplication && this.m_caster != null && this.m_caster.hasCharacteristic(this.m_charac)) {
            this.m_caster.getCharacteristic(this.m_charac).add(this.m_value);
        }
        super.unapplyOverride();
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
    
    @Override
    public void onCheckIn() {
        this.m_percentToTransfer = 0;
        this.m_charac = null;
        this.m_buffReferenceEffect = null;
        this.m_lossReferenceEffect = null;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacTransferToTargets>() {
            @Override
            public CharacTransferToTargets makeObject() {
                return new CharacTransferToTargets();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Par d\u00e9faut, transf\u00e8re tout", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("% \u00e0 transf\u00e9rer", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% \u00e0 transf\u00e9rer", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
