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

public final class CopyCharacMax extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final int NO_VALID_VALUE = 0;
    private byte m_characId;
    private boolean m_targetCopyCaster;
    private int m_characPercent;
    private int m_originalMax;
    private boolean m_addCharacBoost;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    public CopyCharacMax() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(7) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CopyCharacMax.this.m_targetCopyCaster ? 1 : 0));
                buffer.putInt(CopyCharacMax.this.m_originalMax);
                buffer.put(CopyCharacMax.this.m_characId);
                buffer.put((byte)(CopyCharacMax.this.m_addCharacBoost ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CopyCharacMax.this.m_targetCopyCaster = (buffer.get() == 1);
                CopyCharacMax.this.m_originalMax = buffer.getInt();
                CopyCharacMax.this.m_characId = buffer.get();
                CopyCharacMax.this.m_addCharacBoost = (buffer.get() == 1);
            }
        };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CopyCharacMax.PARAMETERS_LIST_SET;
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        CopyCharacMax wre;
        try {
            wre = (CopyCharacMax)CopyCharacMax.m_staticPool.borrowObject();
            wre.m_pool = CopyCharacMax.m_staticPool;
        }
        catch (Exception e) {
            wre = new CopyCharacMax();
            wre.m_pool = null;
            CopyCharacMax.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacDebuff : " + e.getMessage()));
        }
        return wre;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_value == 0) {
            this.setNotified(true);
            return;
        }
        final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId(this.m_characId);
        if (charac == null) {
            CopyCharacMax.m_logger.error((Object)("Id de Charac inconnu " + this.m_characId));
        }
        if (this.m_caster == null || this.m_target == null || charac == null || !this.m_caster.hasCharacteristic(charac) || !this.m_target.hasCharacteristic(charac)) {
            this.setNotified(true);
            return;
        }
        final AbstractCharacteristic destCharac = this.m_targetCopyCaster ? this.m_target.getCharacteristic(charac) : this.m_caster.getCharacteristic(charac);
        if (!this.m_addCharacBoost) {
            destCharac.setMax(this.m_value);
            destCharac.set(destCharac.max());
        }
        else {
            destCharac.updateMaxValue(this.m_value);
            destCharac.set(destCharac.max());
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.extractParams();
        this.m_value = 0;
        final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId(this.m_characId);
        if (charac == null) {
            CopyCharacMax.m_logger.error((Object)("Id de Charac inconnu " + this.m_characId));
            return;
        }
        if (this.m_caster == null || this.m_target == null || !this.m_caster.hasCharacteristic(charac) || !this.m_target.hasCharacteristic(charac)) {
            return;
        }
        final AbstractCharacteristic srcCharac = this.m_targetCopyCaster ? this.m_caster.getCharacteristic(charac) : this.m_target.getCharacteristic(charac);
        final AbstractCharacteristic destCharac = this.m_targetCopyCaster ? this.m_target.getCharacteristic(charac) : this.m_caster.getCharacteristic(charac);
        this.m_originalMax = destCharac.max();
        this.m_value = Math.round(srcCharac.max() * this.m_characPercent / 100.0f);
    }
    
    private void extractParams() {
        this.m_characPercent = 100;
        if (this.m_genericEffect == null) {
            return;
        }
        final int paramsCount = ((WakfuEffect)this.m_genericEffect).getParamsCount();
        if (paramsCount < 1) {
            return;
        }
        final short containerLevel = this.getContainerLevel();
        this.m_characId = (byte)((WakfuEffect)this.m_genericEffect).getParam(0, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (paramsCount < 2) {
            return;
        }
        this.m_targetCopyCaster = (((WakfuEffect)this.m_genericEffect).getParam(1, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        if (paramsCount < 3) {
            return;
        }
        this.m_characPercent = ((WakfuEffect)this.m_genericEffect).getParam(2, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (paramsCount < 4) {
            return;
        }
        this.m_addCharacBoost = (((WakfuEffect)this.m_genericEffect).getParam(3, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
    }
    
    @Override
    public void unapplyOverride() {
        final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId((byte)this.m_value);
        if (this.m_targetCopyCaster && this.m_target != null && this.m_target.hasCharacteristic(charac)) {
            this.m_target.getCharacteristic(charac).setMax(this.m_originalMax);
        }
        else if (this.m_caster != null && this.m_caster.hasCharacteristic(charac)) {
            this.m_caster.getCharacteristic(charac).setMax(this.m_originalMax);
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
        super.onCheckIn();
        this.m_targetCopyCaster = false;
        this.m_characPercent = 100;
        this.m_addCharacBoost = false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CopyCharacMax>() {
            @Override
            public CopyCharacMax makeObject() {
                return new CopyCharacMax();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Copie de Charac entre cible et caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00e0 copier", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("La cible copie le max de la charac du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00e0 copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon ", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("La cible copie le max de la charac du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00e0 copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de la charac \u00e0 copier ", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("La cible copie le max de la charac du caster en % et l'ajoute \u00e0 sa charac de base", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac \u00e0 copier", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("1 pour cible copie caster, 0 sinon ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de la charac \u00e0 copier ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("\u00e9craser(0) ou ajouter(1) \u00e0 la charac de base ?", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
