package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class CharacLossFunctionPaPm extends CharacLoss
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final ObjectPool m_staticPool;
    private int m_remainingAP;
    private int m_remainingMP;
    private boolean m_removeAP;
    private boolean m_removeMP;
    
    public CharacLossFunctionPaPm() {
        super();
    }
    
    public CharacLossFunctionPaPm(final CharacteristicType charac) {
        super(charac);
    }
    
    @Override
    public CharacLossFunctionPaPm newInstance() {
        CharacLossFunctionPaPm re;
        try {
            re = (CharacLossFunctionPaPm)CharacLossFunctionPaPm.m_staticPool.borrowObject();
            re.m_pool = CharacLossFunctionPaPm.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacLossFunctionPaPm(this.m_charac);
            re.m_pool = null;
            CharacLossFunctionPaPm.m_logger.error((Object)("Erreur lors d'un newInstance sur un CharacLossFunctionPaPm : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = 0;
        if (this.m_caster == null) {
            return;
        }
        this.m_remainingAP = this.m_caster.getCharacteristicValue(FighterCharacteristicType.AP);
        this.m_remainingMP = this.m_caster.getCharacteristicValue(FighterCharacteristicType.MP);
        if (this.m_remainingAP == 0 && this.m_remainingMP == 0) {
            return;
        }
        final short containerLevel = this.getContainerLevel();
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 5) {
            final int maxAP = ((WakfuEffect)this.m_genericEffect).getParam(4, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (maxAP > 0) {
                this.m_remainingAP = Math.min(this.m_remainingAP, maxAP);
            }
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 6) {
            final int maxMP = ((WakfuEffect)this.m_genericEffect).getParam(5, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (maxMP > 0) {
                this.m_remainingAP = Math.min(this.m_remainingAP, maxMP);
            }
        }
        final float valuePerAP = ((WakfuEffect)this.m_genericEffect).getParam(0, containerLevel);
        final float valuePerMP = ((WakfuEffect)this.m_genericEffect).getParam(1, containerLevel);
        if (valuePerAP == 0.0f) {
            this.m_remainingAP = 0;
        }
        if (valuePerMP == 0.0f) {
            this.m_remainingMP = 0;
        }
        if (this.m_remainingAP > 0) {
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
                this.m_removeAP = (((WakfuEffect)this.m_genericEffect).getParam(2, containerLevel) == 0.0f);
            }
            else {
                this.m_removeAP = true;
            }
        }
        else {
            this.m_removeAP = false;
        }
        if (this.m_remainingMP > 0) {
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4) {
                this.m_removeMP = (((WakfuEffect)this.m_genericEffect).getParam(3, containerLevel) == 0.0f);
            }
            else {
                this.m_removeMP = true;
            }
        }
        else {
            this.m_removeMP = false;
        }
        this.m_value = Math.round(valuePerAP * this.m_remainingAP + valuePerMP * this.m_remainingMP);
        if (this.m_value == 0) {
            return;
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_caster == null || this.m_target == null) {
            return;
        }
        if (this.m_removeAP || this.m_removeMP) {
            final ActionCost actionCost = ActionCost.checkOut((EffectContext<WakfuEffect>)this.m_context, new SpellCost((byte)(this.m_removeAP ? this.m_remainingAP : 0), (byte)(this.m_removeMP ? this.m_remainingMP : 0), (byte)0), this.m_caster);
            actionCost.setCaster(this.m_caster);
            actionCost.setRunningEffectStatus(RunningEffectStatus.NEUTRAL);
            actionCost.execute(null, false);
        }
        super.executeOverride(triggerRE, trigger);
    }
    
    @Override
    protected int getApplicationProbability() {
        return 100;
    }
    
    @Override
    protected boolean canBeExecutedOnKO() {
        return true;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacLossFunctionPaPm.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Quantit\u00e9 perdue par PA ou par PM (PA/PM consomm\u00e9s)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Qt\u00e9 par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Qt\u00e9 par PM", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Quantit\u00e9 perdue par PA ou par PM (PA et PM peuvent rester)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Qt\u00e9 par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Qt\u00e9 par PM", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Garder les PA (0 = PA enlev\u00e9s(default), 1 = PA pas enlev\u00e9s)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Garder les PM (0 = PM enlev\u00e9s(default), 1 = PM pas enlev\u00e9s)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Max PA Enlevables (<=0 = infini)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Max PM enlevables (<=0 = infini)", WakfuRunningEffectParameterType.CONFIG) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacLossFunctionPaPm>() {
            @Override
            public CharacLossFunctionPaPm makeObject() {
                return new CharacLossFunctionPaPm();
            }
        });
    }
}
