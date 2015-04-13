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

public final class AddCharacValueToAnotherInRealTimeWithThreshold extends AbstractAddCharacValueToAnotherInRealTime
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_threshold;
    private int m_maxModification;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AddCharacValueToAnotherInRealTimeWithThreshold.PARAMETERS_LIST_SET;
    }
    
    public AddCharacValueToAnotherInRealTimeWithThreshold() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(13) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(AddCharacValueToAnotherInRealTimeWithThreshold.this.m_targetCopyCaster ? 1 : 0));
                buffer.putInt(AddCharacValueToAnotherInRealTimeWithThreshold.this.m_threshold);
                buffer.putInt(AddCharacValueToAnotherInRealTimeWithThreshold.this.m_destCharacId);
                buffer.putInt(AddCharacValueToAnotherInRealTimeWithThreshold.this.m_maxModification);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                AddCharacValueToAnotherInRealTimeWithThreshold.this.m_targetCopyCaster = (buffer.get() == 1);
                AddCharacValueToAnotherInRealTimeWithThreshold.this.m_threshold = buffer.getInt();
                AddCharacValueToAnotherInRealTimeWithThreshold.this.m_destCharacId = buffer.getInt();
                AddCharacValueToAnotherInRealTimeWithThreshold.this.m_maxModification = buffer.getInt();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public AddCharacValueToAnotherInRealTimeWithThreshold newInstance() {
        AddCharacValueToAnotherInRealTimeWithThreshold re;
        try {
            re = (AddCharacValueToAnotherInRealTimeWithThreshold)AddCharacValueToAnotherInRealTimeWithThreshold.m_staticPool.borrowObject();
            re.m_pool = AddCharacValueToAnotherInRealTimeWithThreshold.m_staticPool;
        }
        catch (Exception e) {
            re = new AddCharacValueToAnotherInRealTimeWithThreshold();
            re.m_pool = null;
            re.m_isStatic = false;
            AddCharacValueToAnotherInRealTimeWithThreshold.m_logger.error((Object)("Erreur lors d'un checkOut sur un CopyCharacInRealTime : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = -1;
        this.m_targetCopyCaster = false;
        this.m_threshold = 1;
        this.m_maxModification = 0;
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_destCharacId = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 2) {
            this.m_targetCopyCaster = (((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 3) {
            this.m_threshold = ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 4) {
            this.m_maxModification = ((WakfuEffect)this.m_genericEffect).getParam(4, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
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
        this.m_threshold = 1;
        this.m_destCharacId = -1;
        this.m_maxModification = 0;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<AbstractAddCharacValueToAnotherInRealTime>() {
            @Override
            public AbstractAddCharacValueToAnotherInRealTime makeObject() {
                return new AddCharacValueToAnotherInRealTimeWithThreshold();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Copie de Charac entre cible et caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac source", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de la charac destination", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("La cible copie le max de la charac du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00ef¿½ copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de la charac destination", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon (defaut)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Ratio de la valeur a copier", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00ef¿½ copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de la charac destination", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Palier pour 1 point dans la charac destination (defaut = 1)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Ratio de la valeur a copier", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00ef¿½ copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de la charac destination", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Palier pour 1 point dans la charac destination (defaut = 1)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Modification max de la charac destination", WakfuRunningEffectParameterType.CONFIG) }) });
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
            final int valueToAdd = charac.value() / AddCharacValueToAnotherInRealTimeWithThreshold.this.m_threshold;
            int maxedValueToAdd;
            if (AddCharacValueToAnotherInRealTimeWithThreshold.this.m_maxModification > 0) {
                maxedValueToAdd = Math.min(AddCharacValueToAnotherInRealTimeWithThreshold.this.m_maxModification, valueToAdd);
            }
            else {
                maxedValueToAdd = valueToAdd;
            }
            this.m_lastValue = this.m_destCharac.add(maxedValueToAdd);
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
            return "AddValueListener{m_lastValue=" + this.m_lastValue + ", m_destCharac=" + this.m_destCharac + '}';
        }
    }
}
