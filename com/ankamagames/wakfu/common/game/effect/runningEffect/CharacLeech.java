package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class CharacLeech extends CharacDebuff
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_buffInsteadOfGain;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacLeech.PARAMETERS_LIST_SET;
    }
    
    private CharacLeech() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 2;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacLeech.this.m_raiseCurrentValueOnUnapplication ? 1 : 0));
                buffer.put((byte)(CharacLeech.this.m_buffInsteadOfGain ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacLeech.this.m_raiseCurrentValueOnUnapplication = (buffer.get() == 1);
                CharacLeech.this.m_buffInsteadOfGain = (buffer.get() == 1);
            }
        };
    }
    
    public CharacLeech(final FighterCharacteristicType charac) {
        super(charac);
        this.ADDITIONNAL_DATAS = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 2;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacLeech.this.m_raiseCurrentValueOnUnapplication ? 1 : 0));
                buffer.put((byte)(CharacLeech.this.m_buffInsteadOfGain ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacLeech.this.m_raiseCurrentValueOnUnapplication = (buffer.get() == 1);
                CharacLeech.this.m_buffInsteadOfGain = (buffer.get() == 1);
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public CharacLeech newInstance() {
        CharacLeech re;
        try {
            re = (CharacLeech)CharacLeech.m_staticPool.borrowObject();
            re.m_pool = CharacLeech.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacLeech();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacLeech.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacLeech : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public Elements getElement() {
        if (this.m_charac.getCharacteristicType() == 0) {
            final Elements element = ((FighterCharacteristicType)this.m_charac).getRelatedElement();
            if (element != null) {
                return element;
            }
        }
        return Elements.PHYSICAL;
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
                        case AP: {
                            this.m_triggers.set(56);
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
        if (this.m_target == null || !this.m_target.hasCharacteristic(this.m_charac)) {
            this.setNotified(true);
            return;
        }
        if (this.m_buffInsteadOfGain) {
            final int initialCharacteristicValueOnTarget = this.m_target.getCharacteristic(this.m_charac).value();
            if (this.m_value > initialCharacteristicValueOnTarget) {
                this.m_value = initialCharacteristicValueOnTarget;
            }
            this.m_target.getCharacteristic(this.m_charac).add(-this.m_value);
            super.executeOverride(linkedRE, trigger);
            if (this.m_caster != null && this.m_caster.hasCharacteristic(this.m_charac)) {
                final AbstractCharacteristic characteristic = this.m_caster.getCharacteristic(this.m_charac);
                characteristic.updateMaxValue(this.m_value);
                characteristic.add(this.m_value);
            }
        }
        else {
            final int initialCharacteristicValueOnTarget = this.m_target.getCharacteristic(this.m_charac).value();
            final int characLeech = (this.m_value < initialCharacteristicValueOnTarget) ? this.m_value : initialCharacteristicValueOnTarget;
            super.executeOverride(linkedRE, trigger);
            if (this.m_caster != null && this.m_caster.hasCharacteristic(this.m_charac)) {
                this.m_caster.getCharacteristic(this.m_charac).add(characLeech);
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        super.effectiveComputeValue(triggerRE);
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() == 4) {
            this.m_buffInsteadOfGain = (0 == ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.RANDOM));
        }
        else {
            this.m_buffInsteadOfGain = false;
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_buffInsteadOfGain && this.m_executed && this.m_caster != null && this.m_caster.hasCharacteristic(this.m_charac)) {
            final AbstractCharacteristic characteristic = this.m_caster.getCharacteristic(this.m_charac);
            characteristic.add(-this.m_value);
            characteristic.updateMaxValue(-this.m_value);
        }
        super.unapplyOverride();
    }
    
    @Override
    public boolean useCaster() {
        return true;
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
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacLeech>() {
            @Override
            public CharacLeech makeObject() {
                return new CharacLeech((CharacLeech$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Vol de Charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Vol de Charac et prise en compte des resist", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Proba d'application en % (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Vol de charac param\u00e9tr\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Proba d'application en % (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Augmente la valeur courante \u00e0 la d\u00e9sapplication (0 non / 1 oui default", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Choix entre Gain et buff pour le caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Proba d'application en % (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Augmente la valeur courante \u00e0 la d\u00e9sapplication (0 non / 1 oui default", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Gain ou Buff caster (0 buff / 1 gain (default)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
