package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public abstract class CharacModification extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected CharacteristicType m_charac;
    protected boolean m_valuePerCentOfCurrentValue;
    private final BinarSerialPart ADDITIONAL_DATA;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacModification.PARAMETERS_LIST_SET;
    }
    
    protected CharacModification() {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 1;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                if (CharacModification.this.m_charac != null) {
                    buffer.put(CharacModification.this.m_charac.getId());
                }
                else {
                    buffer.put((byte)0);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacModification.this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(buffer.get());
            }
        };
    }
    
    protected CharacModification(final CharacteristicType charac) {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 1;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                if (CharacModification.this.m_charac != null) {
                    buffer.put(CharacModification.this.m_charac.getId());
                }
                else {
                    buffer.put((byte)0);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacModification.this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(buffer.get());
            }
        };
        this.m_charac = charac;
        this.m_valuePerCentOfCurrentValue = false;
        this.setTriggersToExecute();
    }
    
    protected CharacModification(final CharacteristicType charac, final boolean valuePerCentOfCurrentValue) {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 1;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                if (CharacModification.this.m_charac != null) {
                    buffer.put(CharacModification.this.m_charac.getId());
                }
                else {
                    buffer.put((byte)0);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacModification.this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(buffer.get());
            }
        };
        this.m_charac = charac;
        this.m_valuePerCentOfCurrentValue = valuePerCentOfCurrentValue;
        this.setTriggersToExecute();
    }
    
    public CharacteristicType getCharacteristicType() {
        return this.m_charac;
    }
    
    @Override
    public boolean canBeExecuted() {
        return this.m_value != 0 && super.canBeExecuted();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.modificationIsNotApplicable()) {
            this.setNotified(true);
            return;
        }
        final AbstractCharacteristic charac = this.extractTargetCharac();
        if (charac == null) {
            this.setNotified(true);
            return;
        }
        if (this.m_valuePerCentOfCurrentValue && this.isValueComputationEnabled()) {
            this.convertValueFromPercentOfCurrent(charac);
        }
        this.applyValueModification(charac);
        this.updateTimelineIfNecessary();
        if (this.isValueComputationEnabled() && EffectProbabilityModificators.isEffectConcerned(this)) {
            this.notifyExecution(linkedRE, trigger);
            EffectProbabilityModificators.applyProbabilityManagementState(this);
        }
    }
    
    @Override
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
        switch (whatToUpdate) {
            case 0: {
                if (!set) {
                    this.m_value += ValueRounder.randomRound(this.m_value * howMuchToUpate / 100.0f);
                    break;
                }
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
        if (this.m_value == 0) {
            this.setNotified();
        }
    }
    
    boolean modificationIsNotApplicable() {
        return this.m_charac == null || this.getEffectExecutionTarget() == null || !this.getEffectExecutionTarget().hasCharacteristic(this.m_charac);
    }
    
    private AbstractCharacteristic extractTargetCharac() {
        AbstractCharacteristic charac = null;
        switch (this.m_charac.getCharacteristicType()) {
            case 0: {
                charac = this.getEffectExecutionTarget().getCharacteristic(this.m_charac);
                break;
            }
            default: {
                CharacModification.m_logger.warn((Object)("On veut appliquer une modification de charac sur autre chose qu'une fighterCharac " + this.m_charac));
                break;
            }
        }
        return charac;
    }
    
    private void convertValueFromPercentOfCurrent(final AbstractCharacteristic charac) {
        this.m_value *= (int)(charac.value() / 100.0f);
    }
    
    abstract void applyValueModification(final AbstractCharacteristic p0);
    
    private void updateTimelineIfNecessary() {
        if (this.m_charac == FighterCharacteristicType.INIT && this.m_context != null && this.m_context.getTimeline() != null) {
            this.m_context.getTimeline().updateDynamicOrder();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final short level = this.getContainerLevel();
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 4) {
            this.m_value = DiceRoll.roll(((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL), ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL), ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL));
        }
        else {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 1) {
                this.m_valuePerCentOfCurrentValue = (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
            }
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 2) {
                final int characId = ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                if (characId != -1 || this.m_charac == null) {
                    final FighterCharacteristicType fromId = FighterCharacteristicType.getCharacteristicTypeFromId((byte)characId);
                    if (fromId == null) {
                        CharacModification.m_logger.error((Object)("Param\u00e9trage foireux d'un effet de modification de charac " + ((WakfuEffect)this.m_genericEffect).getEffectId()));
                    }
                    else {
                        this.m_charac = fromId;
                    }
                }
            }
        }
        if (this.m_value == -1) {
            WakfuRunningEffect trigger = (WakfuRunningEffect)triggerRE;
            if (trigger == null) {
                trigger = ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect();
            }
            if (trigger == null) {
                CharacModification.m_logger.error((Object)"On ne peut pas copier la valeur de l'effet declencheur, celui-ci est inconnu");
                this.m_value = 0;
                return;
            }
            if (trigger instanceof ActionCost) {
                final ActionCost actionCost = (ActionCost)trigger;
                final byte type = this.m_charac.getId();
                if (type == FighterCharacteristicType.AP.getId()) {
                    this.m_value = actionCost.getApUseFromValue();
                }
                else if (type == FighterCharacteristicType.MP.getId()) {
                    this.m_value = actionCost.getMpUseFromValue();
                }
                else if (type == FighterCharacteristicType.WP.getId()) {
                    this.m_value = actionCost.getWpUseFromValue();
                }
            }
            else {
                this.m_value = trigger.getValue();
            }
        }
    }
    
    @Override
    public final void unapplyOverride() {
        if (this.m_executed && this.hasDuration() && this.m_charac != null) {
            switch (this.m_charac.getCharacteristicType()) {
                case 0: {
                    if (this.getEffectExecutionTarget() != null && this.getEffectExecutionTarget().hasCharacteristic(this.m_charac)) {
                        this.rollbackCharacModification();
                        this.updateTimelineIfNecessary();
                        break;
                    }
                    break;
                }
            }
        }
        super.unapplyOverride();
    }
    
    abstract void rollbackCharacModification();
    
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
        if (this.m_charac != null && this.m_charac.getCharacteristicType() == 0) {
            final Elements element = ((FighterCharacteristicType)this.m_charac).getRelatedElement();
            if (element != null) {
                return element;
            }
        }
        return Elements.PHYSICAL;
    }
    
    @Override
    protected boolean checkIsNotValidTargetProperty() {
        return this.isNotValidTargetAndContainerNotSpecialState();
    }
    
    protected EffectUser getEffectExecutionTarget() {
        return this.m_target;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATA;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Modif de Charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (eventuellement en %) (-1 pour valeur de l'effet declencheur)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Modif de Charac variable", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("+", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("(fake, sert \u00e0 rien, laisser \u00e0 0)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Specification du type de Modif (% ou fixe)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (eventuellement en %) (-1 pour valeur de l'effet declencheur)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Fixe = 0; %=1", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Avec id de la charac (cf FighterCharacteriticType)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (eventuellement en %) (-1 pour valeur de l'effet declencheur)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Fixe = 0; %=1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de la charac", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Avec % d'application", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (eventuellement en %) (-1 pour valeur de l'effet declencheur)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Fixe = 0; %=1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de la charac (defaut = -1)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% d'application (defaut = -1)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("(fake, sert \u00e0 rien, laisser \u00e0 0)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
