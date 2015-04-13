package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

abstract class AbstractAddCharacValueToAnotherInRealTime extends WakfuRunningEffect
{
    protected int m_destCharacId;
    protected boolean m_targetCopyCaster;
    private CharacteristicUpdateListenerWithCancel m_listener;
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        final FighterCharacteristicType srcCharacType = FighterCharacteristicType.getCharacteristicTypeFromId((byte)this.m_value);
        if (srcCharacType == null) {
            this.setNotified();
            AbstractAddCharacValueToAnotherInRealTime.m_logger.error((Object)("Erreur de saisie, charac inexistante " + this.m_value));
            return;
        }
        final FighterCharacteristicType destCharacType = FighterCharacteristicType.getCharacteristicTypeFromId((byte)this.m_destCharacId);
        if (srcCharacType == null) {
            this.setNotified();
            AbstractAddCharacValueToAnotherInRealTime.m_logger.error((Object)("Erreur de saisie, charac inexistante " + this.m_value));
            return;
        }
        final FighterCharacteristic srcCharac = (FighterCharacteristic)(this.m_targetCopyCaster ? this.m_caster.getCharacteristic(srcCharacType) : this.m_target.getCharacteristic(srcCharacType));
        final FighterCharacteristic destCharac = (FighterCharacteristic)(this.m_targetCopyCaster ? this.m_target.getCharacteristic(destCharacType) : this.m_caster.getCharacteristic(destCharacType));
        if (destCharac == null || srcCharac == null) {
            this.setNotified();
            return;
        }
        srcCharac.addListener(this.m_listener = this.getListener(destCharac, srcCharac));
        this.m_listener.onCharacteristicUpdated(srcCharac);
    }
    
    protected abstract CharacteristicUpdateListenerWithCancel getListener(final FighterCharacteristic p0, final FighterCharacteristic p1);
    
    @Override
    public void unapplyOverride() {
        if (!this.hasDuration()) {
            return;
        }
        final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId((byte)this.m_value);
        if (charac == null) {
            return;
        }
        if (this.m_listener != null) {
            this.m_listener.cancel();
            this.m_listener.unregister();
        }
        super.unapplyOverride();
    }
    
    @Override
    public void onCheckIn() {
        if (this.m_listener != null) {
            this.m_listener.unregister();
        }
        this.m_listener = null;
        super.onCheckIn();
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
    
    protected interface CharacteristicUpdateListenerWithCancel extends CharacteristicUpdateListener
    {
        void cancel();
        
        void unregister();
    }
}
