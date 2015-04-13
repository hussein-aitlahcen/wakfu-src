package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CopyCharacInRealTime extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private CharacteristicUpdateListener m_listener;
    private int m_percentToCopy;
    private boolean m_targetCopyCaster;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CopyCharacInRealTime.PARAMETERS_LIST_SET;
    }
    
    public CopyCharacInRealTime() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(5) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CopyCharacInRealTime.this.m_targetCopyCaster ? 1 : 0));
                buffer.putInt(CopyCharacInRealTime.this.m_percentToCopy);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CopyCharacInRealTime.this.m_targetCopyCaster = (buffer.get() == 1);
                CopyCharacInRealTime.this.m_percentToCopy = buffer.getInt();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public CopyCharacInRealTime newInstance() {
        CopyCharacInRealTime re;
        try {
            re = (CopyCharacInRealTime)CopyCharacInRealTime.m_staticPool.borrowObject();
            re.m_pool = CopyCharacInRealTime.m_staticPool;
        }
        catch (Exception e) {
            re = new CopyCharacInRealTime();
            re.m_pool = null;
            re.m_isStatic = false;
            CopyCharacInRealTime.m_logger.error((Object)("Erreur lors d'un checkOut sur un CopyCharacInRealTime : " + e.getMessage()));
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
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 1) {
            this.m_targetCopyCaster = (((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 2) {
            this.m_percentToCopy = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId((byte)this.m_value);
        if (charac == null) {
            this.setNotified();
            CopyCharacInRealTime.m_logger.error((Object)("Erreur de saisie, charac inexistante " + this.m_value));
            return;
        }
        final FighterCharacteristic srcCharac = (FighterCharacteristic)(this.m_targetCopyCaster ? this.m_caster.getCharacteristic(charac) : this.m_target.getCharacteristic(charac));
        final FighterCharacteristic destCharac = (FighterCharacteristic)(this.m_targetCopyCaster ? this.m_target.getCharacteristic(charac) : this.m_caster.getCharacteristic(charac));
        if (destCharac == null || srcCharac == null) {
            this.setNotified();
            return;
        }
        srcCharac.addListener(this.m_listener = new CharacteristicUpdateListener() {
            @Override
            public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
                if (CopyCharacInRealTime.this.m_percentToCopy == 100) {
                    destCharac.copyMinMaxValueAndMaxPercentModificator((FighterCharacteristic)charac);
                }
                else {
                    destCharac.copyMinMaxValueAndMaxPercentModificator((FighterCharacteristic)charac, CopyCharacInRealTime.this.m_percentToCopy);
                }
            }
        });
        if (this.m_percentToCopy == 100) {
            destCharac.copyMinMaxValueAndMaxPercentModificator(srcCharac);
        }
        else {
            destCharac.copyMinMaxValueAndMaxPercentModificator(srcCharac, this.m_percentToCopy);
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (!this.hasDuration()) {
            return;
        }
        final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId((byte)this.m_value);
        if (charac == null) {
            return;
        }
        final AbstractCharacteristic srcCharac = this.m_targetCopyCaster ? this.m_caster.getCharacteristic(charac) : this.m_target.getCharacteristic(charac);
        if (srcCharac != null) {
            srcCharac.removeListener(this.m_listener);
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
    
    @Override
    public void onCheckIn() {
        this.m_targetCopyCaster = false;
        this.m_percentToCopy = 100;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CopyCharacInRealTime>() {
            @Override
            public CopyCharacInRealTime makeObject() {
                return new CopyCharacInRealTime();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Copie de Charac entre cible et caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00e0 copier", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("La cible copie le max de la charac du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00e0 copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon (defaut)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Ratio de la valeur a copier", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00e0 copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% \u00e0 copier, (defaut = 100)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
