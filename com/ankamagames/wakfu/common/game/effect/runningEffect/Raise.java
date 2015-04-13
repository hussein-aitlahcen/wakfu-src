package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class Raise extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_virtualHpGain;
    private boolean m_addVirtualHp;
    private boolean m_setApMpToMax;
    private final BinarSerialPart m_additionalData;
    private boolean m_withPercentLife;
    private int m_percent;
    private boolean m_checkController;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Raise.PARAMETERS_LIST_SET;
    }
    
    public Raise() {
        super();
        this.m_additionalData = new BinarSerialPart(6) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(Raise.this.m_virtualHpGain);
                buffer.put((byte)(Raise.this.m_addVirtualHp ? 1 : 0));
                buffer.put((byte)(Raise.this.m_setApMpToMax ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                Raise.this.m_virtualHpGain = buffer.getInt();
                Raise.this.m_addVirtualHp = (buffer.get() == 1);
                Raise.this.m_setApMpToMax = (buffer.get() == 1);
            }
        };
        this.m_withPercentLife = false;
        this.m_percent = 0;
        this.setTriggersToExecute();
    }
    
    @Override
    public Raise newInstance() {
        Raise re;
        try {
            re = (Raise)Raise.m_staticPool.borrowObject();
            re.m_pool = Raise.m_staticPool;
        }
        catch (Exception e) {
            re = new Raise();
            re.m_isStatic = false;
            re.m_pool = null;
            Raise.m_logger.error((Object)("Erreur lors d'un checkOut sur un RE:Raise : " + e.getMessage()));
        }
        this.m_maxExecutionCount = 1;
        re.m_checkController = this.m_checkController;
        re.m_percent = this.m_percent;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(202);
        this.m_triggers.set(1);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_checkController && this.m_target instanceof BasicCharacterInfo) {
            final BasicCharacterInfo originalController = ((BasicCharacterInfo)this.m_target).getOriginalController();
            if (originalController != null && !originalController.isInPlay()) {
                this.setNotified();
                return;
            }
        }
        if (this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HP) && !this.m_target.isActiveProperty(FightPropertyType.CANNOT_BE_RAISED)) {
            this.raiseAndGiveHp();
        }
        else {
            this.setNotified(true);
        }
    }
    
    void raiseAndGiveHp() {
        if (this.m_withPercentLife && this.isValueComputationEnabled()) {
            final int max = this.m_target.getCharacteristic(FighterCharacteristicType.HP).max();
            this.m_value = Math.max(1, ValueRounder.randomRound(max / 100.0f * this.m_percent));
            if (this.m_target.hasCharacteristic(FighterCharacteristicType.VIRTUAL_HP)) {
                final int virtualHpMax = this.m_target.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP).max();
                this.m_virtualHpGain = Math.max(1, ValueRounder.randomRound(virtualHpMax / 100.0f * this.m_percent));
            }
        }
        this.m_target.getCharacteristic(FighterCharacteristicType.HP).set(this.m_value);
        if (this.m_addVirtualHp && this.m_target.hasCharacteristic(FighterCharacteristicType.VIRTUAL_HP)) {
            this.m_target.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP).set(this.m_virtualHpGain);
        }
        if (this.m_target instanceof BasicCharacterInfo && this.getParams() != null && ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect() != null) {
            ((BasicCharacterInfo)this.m_target).setHasBeenRaisedByTrigger(true);
        }
        if (this.m_setApMpToMax) {
            if (this.m_target.hasCharacteristic(FighterCharacteristicType.AP)) {
                this.m_target.getCharacteristic(FighterCharacteristicType.AP).toMax();
            }
            if (this.m_target.hasCharacteristic(FighterCharacteristicType.MP)) {
                this.m_target.getCharacteristic(FighterCharacteristicType.MP).toMax();
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_checkController = false;
        this.m_percent = 0;
        final short level = this.getContainerLevel();
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() <= 0) {
            this.m_value = 1;
            return;
        }
        this.m_percent = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_withPercentLife = true;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 1) {
            this.m_checkController = (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 2) {
            this.m_addVirtualHp = (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 3) {
            this.m_setApMpToMax = (((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
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
    protected boolean canBeExecutedOnKO() {
        return true;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.m_additionalData;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_withPercentLife = false;
        this.m_percent = 0;
        this.m_checkController = false;
        this.m_virtualHpGain = 0;
        this.m_addVirtualHp = false;
        this.m_setApMpToMax = false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<Raise>() {
            @Override
            public Raise makeObject() {
                return new Raise();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Pas de param", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("% de pdv restor\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("%", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Uniquement si le controlleur original est encore en jeu", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("%", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("controleur encore en jeu (1 = oui, 0 == non (defaut))", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Restore aussi les pvs virtuels", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("%", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("controleur encore en jeu (1 = oui, 0 == non (defaut))", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("restore aussi les pvs virtuels (1 = oui, 0 == non (defaut))", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Met les PA et PM au max", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("%", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("controleur encore en jeu (1 = oui, 0 == non (defaut))", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("restore aussi les pvs virtuels (1 = oui, 0 == non (defaut))", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("PA/PM au max (1 = oui, 0 == non (defaut))", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
