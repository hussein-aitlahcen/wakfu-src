package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public final class AddCharacValueToAnotherInRealTime extends AbstractAddCharacValueToAnotherInRealTime
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_percentToCopy;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AddCharacValueToAnotherInRealTime.PARAMETERS_LIST_SET;
    }
    
    public AddCharacValueToAnotherInRealTime() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(9) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(AddCharacValueToAnotherInRealTime.this.m_targetCopyCaster ? 1 : 0));
                buffer.putInt(AddCharacValueToAnotherInRealTime.this.m_percentToCopy);
                buffer.putInt(AddCharacValueToAnotherInRealTime.this.m_destCharacId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                AddCharacValueToAnotherInRealTime.this.m_targetCopyCaster = (buffer.get() == 1);
                AddCharacValueToAnotherInRealTime.this.m_percentToCopy = buffer.getInt();
                AddCharacValueToAnotherInRealTime.this.m_destCharacId = buffer.getInt();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public AddCharacValueToAnotherInRealTime newInstance() {
        AddCharacValueToAnotherInRealTime re;
        try {
            re = (AddCharacValueToAnotherInRealTime)AddCharacValueToAnotherInRealTime.m_staticPool.borrowObject();
            re.m_pool = AddCharacValueToAnotherInRealTime.m_staticPool;
        }
        catch (Exception e) {
            re = new AddCharacValueToAnotherInRealTime();
            re.m_pool = null;
            re.m_isStatic = false;
            AddCharacValueToAnotherInRealTime.m_logger.error((Object)("Erreur lors d'un checkOut sur un CopyCharacInRealTime : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = -1;
        this.m_targetCopyCaster = false;
        this.m_percentToCopy = 100;
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_destCharacId = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 2) {
            this.m_targetCopyCaster = (((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 3) {
            this.m_percentToCopy = ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    protected CharacteristicUpdateListenerWithCancel getListener(final FighterCharacteristic destCharac, final FighterCharacteristic srcCharac) {
        return new AddValueListener(destCharac, srcCharac);
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    @Override
    public void onCheckIn() {
        this.m_targetCopyCaster = false;
        this.m_percentToCopy = 100;
        this.m_destCharacId = -1;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<AbstractAddCharacValueToAnotherInRealTime>() {
            @Override
            public AbstractAddCharacValueToAnotherInRealTime makeObject() {
                return new AddCharacValueToAnotherInRealTime();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Copie de Charac entre cible et caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac source", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de la charac destination", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("La cible copie le max de la charac du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00e0 copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de la charac destination", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon (defaut)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Ratio de la valeur a copier", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00e0 copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de la charac destination", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% \u00e0 copier, (defaut = 100)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
    
    private class AddValueListener implements CharacteristicUpdateListenerWithCancel
    {
        private int m_lastValue;
        private final FighterCharacteristic m_destCharac;
        private final FighterCharacteristic m_srcCharac;
        
        private AddValueListener(final FighterCharacteristic destCharac, final FighterCharacteristic srcCharac) {
            super();
            this.m_destCharac = destCharac;
            this.m_srcCharac = srcCharac;
        }
        
        @Override
        public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
            this.m_destCharac.substract(this.m_lastValue);
            final int valueToAdd = charac.value() * AddCharacValueToAnotherInRealTime.this.m_percentToCopy / 100;
            this.m_lastValue = this.m_destCharac.add(valueToAdd);
        }
        
        @Override
        public void cancel() {
            this.m_destCharac.substract(this.m_lastValue);
        }
        
        @Override
        public void unregister() {
            this.m_srcCharac.removeListener(this);
        }
        
        @Override
        public String toString() {
            return "AddValueListener{m_lastValue=" + this.m_lastValue + ", m_destCharac=" + this.m_destCharac + ", m_srcCharac=" + this.m_srcCharac + '}';
        }
    }
}
