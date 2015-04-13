package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class CharacSteal extends CharacLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_WHEN_CHARAC_SET;
    private static final ParameterListSet PARAMETERS_LIST_SET_WHEN_CHARAC_DYNAMIC;
    private boolean m_dynamicCharacteristic;
    private boolean m_buffInsteadOfGain;
    private boolean m_isDurationInFullTurns;
    private boolean m_endsAtEndOfTurn;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        if (this.m_dynamicCharacteristic) {
            return CharacSteal.PARAMETERS_LIST_SET_WHEN_CHARAC_DYNAMIC;
        }
        return CharacSteal.PARAMETERS_LIST_SET_WHEN_CHARAC_SET;
    }
    
    public CharacSteal() {
        super();
        this.m_dynamicCharacteristic = false;
        this.m_isDurationInFullTurns = false;
        this.m_endsAtEndOfTurn = true;
        this.ADDITIONNAL_DATAS = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 2;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacSteal.this.m_buffInsteadOfGain ? 1 : 0));
                if (CharacSteal.this.m_charac != null) {
                    buffer.put(CharacSteal.this.m_charac.getId());
                }
                else {
                    buffer.put((byte)0);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacSteal.this.m_buffInsteadOfGain = (buffer.get() == 1);
                CharacSteal.this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(buffer.get());
            }
        };
        this.m_dynamicCharacteristic = true;
        this.m_isDurationInFullTurns = false;
        this.m_endsAtEndOfTurn = true;
    }
    
    public CharacSteal(final FighterCharacteristicType charac) {
        super(charac);
        this.m_dynamicCharacteristic = false;
        this.m_isDurationInFullTurns = false;
        this.m_endsAtEndOfTurn = true;
        this.ADDITIONNAL_DATAS = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 2;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacSteal.this.m_buffInsteadOfGain ? 1 : 0));
                if (CharacSteal.this.m_charac != null) {
                    buffer.put(CharacSteal.this.m_charac.getId());
                }
                else {
                    buffer.put((byte)0);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacSteal.this.m_buffInsteadOfGain = (buffer.get() == 1);
                CharacSteal.this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(buffer.get());
            }
        };
        this.m_dynamicCharacteristic = false;
        this.m_isDurationInFullTurns = false;
        this.m_endsAtEndOfTurn = true;
        this.setTriggersToExecute();
    }
    
    @Override
    public CharacSteal newInstance() {
        CharacSteal re;
        try {
            re = (CharacSteal)CharacSteal.m_staticPool.borrowObject();
            re.m_pool = CharacSteal.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacSteal();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacSteal.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacSteal : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        re.m_dynamicCharacteristic = this.m_dynamicCharacteristic;
        re.m_isDurationInFullTurns = this.m_isDurationInFullTurns;
        re.m_endsAtEndOfTurn = this.m_endsAtEndOfTurn;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final short level = this.getContainerLevel();
        if (this.m_dynamicCharacteristic) {
            this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId((byte)((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL));
            if (this.m_charac == null) {
                return;
            }
        }
        final int firstParam = this.m_dynamicCharacteristic ? 1 : 0;
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(firstParam, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > firstParam + 1) {
            this.m_buffInsteadOfGain = (((WakfuEffect)this.m_genericEffect).getParam(firstParam + 1) == 1.0f);
        }
        else {
            this.m_buffInsteadOfGain = false;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 5) {
            this.m_buffInsteadOfGain = true;
            this.m_endsAtEndOfTurn = (((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
            this.m_isDurationInFullTurns = (((WakfuEffect)this.m_genericEffect).getParam(4, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        else {
            this.m_isDurationInFullTurns = false;
            this.m_endsAtEndOfTurn = true;
        }
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        if (this.m_charac == null) {
            return;
        }
        Label_0081: {
            switch (this.m_charac.getCharacteristicType()) {
                case 0: {
                    switch ((FighterCharacteristicType)this.m_charac) {
                        case WP: {
                            this.m_triggers.set(59);
                            break Label_0081;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final EffectUser target = this.getEffectExecutionTarget();
        if (target == null || !target.hasCharacteristic(this.m_charac) || this.m_caster == null || !this.m_caster.hasCharacteristic(this.m_charac)) {
            this.setNotified(true);
        }
        else {
            if (this.isValueComputationEnabled()) {
                if (this.m_value == -1) {
                    this.m_value = target.getCharacteristicValue(this.m_charac);
                }
                else {
                    this.m_value = ((this.m_value < target.getCharacteristicValue(this.m_charac)) ? this.m_value : target.getCharacteristicValue(this.m_charac));
                }
            }
            if (this.m_buffInsteadOfGain) {
                this.notifyExecution(linkedRE, trigger);
                final CharacBuff characBuff = new CharacBuff(this.m_charac);
                characBuff.setTarget(this.m_caster);
                characBuff.disableValueComputation();
                characBuff.forceValue(this.m_value);
                characBuff.setAddCurrentValue(true);
                characBuff.setParent(this);
                characBuff.setCaster(this.m_caster);
                ((RunningEffect<DefaultFightOneTurnEffect, EC>)characBuff).setGenericEffect(DefaultFightOneTurnEffect.makeWithTurnDetails(this.m_isDurationInFullTurns, this.m_endsAtEndOfTurn));
                characBuff.setContext(this.m_context);
                characBuff.applyOnTargets(this.m_caster);
            }
            else {
                this.m_caster.getCharacteristic(this.m_charac).add(this.m_value);
            }
        }
        super.executeOverride(linkedRE, trigger);
    }
    
    @Override
    protected int getApplicationProbability() {
        return 100;
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacSteal>() {
            @Override
            public CharacSteal makeObject() {
                return new CharacSteal();
            }
        });
        PARAMETERS_LIST_SET_WHEN_CHARAC_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Vol de Charac (Gain pour caster)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (-1 pour valeur restante dans la charac)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Vol de Charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (-1 pour valeur restante dans la charac)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Buff (1) ou Gain (0=default) pour caster", WakfuRunningEffectParameterType.VALUE) }) });
        PARAMETERS_LIST_SET_WHEN_CHARAC_DYNAMIC = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Vol de Charac (Gain pour caster)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Charac", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur (-1 pour valeur restante dans la charac)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Vol de Charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Charac", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur (-1 pour valeur restante dans la charac)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Buff (1) ou Gain (0=default) pour caster", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Vol de Charac, buff poru le caster, avec def des params pour la fin de l'effet de buff", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Charac", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur (-1 pour valeur restante dans la charac)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Buff pour caster (pas utilis\u00e9, buff forc\u00e9)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Buff se termine en fin de tour (0/1)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Buff en tours complets (0/1)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
