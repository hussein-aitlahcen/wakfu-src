package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.hpLoss.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class CharacPoison extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private Elements m_element;
    private FighterCharacteristicType m_charac;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacPoison.PARAMETERS_LIST_SET;
    }
    
    public CharacPoison(final FighterCharacteristicType charac) {
        super();
        this.setTriggersToExecute();
        this.m_charac = charac;
    }
    
    private CharacPoison() {
        super();
    }
    
    @Override
    public CharacPoison newInstance() {
        CharacPoison re;
        try {
            re = (CharacPoison)CharacPoison.m_staticPool.borrowObject();
            re.m_pool = CharacPoison.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacPoison();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacPoison.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.m_target.getCharacteristic(FighterCharacteristicType.HP).substract(this.m_value);
        }
        else {
            this.setNotified(true);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.extractParams();
        this.extractBaseValue(triggerRE);
        this.computeValueModifications(triggerRE);
    }
    
    private void extractParams() {
        this.m_element = null;
        if (this.m_genericEffect == null) {
            return;
        }
        final short level = this.getContainerLevel();
        this.m_value = ValueRounder.randomRound(((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM));
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            final byte elementId = (byte)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
            this.m_element = Elements.getElementFromId(elementId);
        }
    }
    
    private void computeValueModifications(final RunningEffect triggerRE) {
        if (this.m_element == null) {
            this.m_element = Elements.WATER;
        }
        int conditions = 0;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            conditions = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        final HpLossComputer computer = new HpLossComputerImpl(this.m_caster, this.m_target, this.m_element, (WakfuEffect)this.m_genericEffect);
        computer.setConditions(conditions);
        computer.setValue(this.m_value);
        computer.setAffectedByLocalisation(this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).isAffectedByLocalisation());
        computer.computeWithModificator();
        RunningEffectUtils.setTriggerForElement(computer.getElementForResistance(), this);
        this.m_value = computer.getValue();
    }
    
    private void extractBaseValue(final RunningEffect triggerRE) {
        if (triggerRE != null) {
            if (triggerRE instanceof ActionCost) {
                switch (this.m_charac) {
                    case AP: {
                        this.m_value *= ((ActionCost)triggerRE).getApUseFromValue();
                        break;
                    }
                    case MP: {
                        this.m_value *= ((ActionCost)triggerRE).getMpUseFromValue();
                        break;
                    }
                    case WP: {
                        this.m_value *= ((ActionCost)triggerRE).getWpUseFromValue();
                        break;
                    }
                }
            }
            else if (triggerRE instanceof CharacDebuff || triggerRE instanceof CharacLoss) {
                this.m_value *= triggerRE.getValue();
            }
            else if (triggerRE.getTriggersToExecute() != null && triggerRE.getTriggersToExecute().get(2198)) {
                this.m_value *= triggerRE.getValue();
            }
        }
    }
    
    @Override
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
        super.update(whatToUpdate, howMuchToUpate, set);
        switch (whatToUpdate) {
            case 0: {
                if (!set) {
                    this.m_value += ValueRounder.randomRound(this.m_value * howMuchToUpate / 100.0f);
                    break;
                }
                this.m_value = ValueRounder.randomRound(this.m_value * howMuchToUpate / 100.0f);
                break;
            }
            case 1: {
                if (set) {
                    this.m_value = ValueRounder.randomRound(howMuchToUpate);
                    break;
                }
                this.m_value += (int)howMuchToUpate;
                break;
            }
        }
        this.m_value = Math.max(0, this.m_value);
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
    public Elements getElement() {
        return this.m_element;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacPoison>() {
            @Override
            public CharacPoison makeObject() {
                return new CharacPoison((CharacPoison$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Poison sur charac (trigger)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("PdV/point de charac", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Avec Element", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("PdV/point de charac", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Avec modificateur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("PdV/point de charac", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("mod : boost(1) / res (2) / rebound (4) / absorb(8) ", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
