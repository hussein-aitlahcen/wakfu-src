package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ActionCost extends DynamicallyDefinedRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_isForMovement;
    private boolean m_isForSpell;
    
    public ActionCost() {
        super();
        this.m_isForMovement = false;
        this.m_isForSpell = false;
        this.setTriggersToExecute();
    }
    
    @Override
    public ActionCost newInstance() {
        ActionCost re;
        try {
            re = (ActionCost)ActionCost.m_staticPool.borrowObject();
            re.m_pool = ActionCost.m_staticPool;
        }
        catch (Exception e) {
            re = new ActionCost();
            re.m_isStatic = false;
            re.m_pool = null;
            ActionCost.m_logger.error((Object)("Erreur lors d'un newInstance sur un ActionCost : " + e.getMessage()));
        }
        return re;
    }
    
    public static ActionCost checkOut(final EffectContext<WakfuEffect> context, final SpellCost cost, final EffectUser target) {
        ActionCost re;
        try {
            re = (ActionCost)ActionCost.m_staticPool.borrowObject();
            re.m_pool = ActionCost.m_staticPool;
        }
        catch (Exception e) {
            re = new ActionCost();
            re.m_isStatic = false;
            re.m_pool = null;
            ActionCost.m_logger.error((Object)("Erreur lors d'un checkOut sur un ActionCost : " + e.getMessage()));
        }
        re.m_id = RunningEffectConstants.ACTION_COST.getId();
        re.m_status = RunningEffectConstants.ACTION_COST.getObject().getRunningEffectStatus();
        final byte apUsed = cost.getCharacCost(FighterCharacteristicType.AP);
        final byte mpUsed = cost.getCharacCost(FighterCharacteristicType.MP);
        final byte wpUsed = cost.getCharacCost(FighterCharacteristicType.WP);
        final byte chrageUsed = cost.getCharacCost(FighterCharacteristicType.CHRAGE);
        re.updateValue(apUsed, mpUsed, wpUsed, chrageUsed);
        re.setTriggersToExecute();
        re.m_target = target;
        re.m_maxExecutionCount = -1;
        re.m_context = (EffectContext<FX>)context;
        return re;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ActionCost.PARAMETERS_LIST_SET;
    }
    
    private void updateValue(final byte apCost, final byte mpCost, final byte wpCost, final byte chrageCost) {
        this.m_value = (chrageCost << 24 | wpCost << 16 | mpCost << 8 | apCost);
    }
    
    public byte getWpUseFromValue() {
        return (byte)((this.m_value & 0xFF0000) >> 16);
    }
    
    public byte getMpUseFromValue() {
        return (byte)((this.m_value & 0xFF00) >> 8);
    }
    
    public byte getApUseFromValue() {
        return (byte)(this.m_value & 0xFF);
    }
    
    public byte getChrageUseFromValue() {
        return (byte)((this.m_value & 0xFF000000) >> 24);
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        if (this.getApUseFromValue() > 0) {
            this.m_triggers.set(55);
        }
        if (this.getMpUseFromValue() > 0) {
            this.m_triggers.set(65);
        }
        if (this.getWpUseFromValue() > 0) {
            this.m_triggers.set(60);
        }
        if (this.getChrageUseFromValue() > 0) {
            this.m_triggers.set(73);
        }
        if (this.m_isForMovement) {
            this.m_triggers.set(74);
        }
        if (this.m_isForSpell && this.getMpUseFromValue() > 0) {
            this.m_triggers.set(2188);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final byte apCost = this.consumeCharac(this.getApUseFromValue(), FighterCharacteristicType.AP);
        final byte mpCost = this.consumeCharac(this.getMpUseFromValue(), FighterCharacteristicType.MP);
        final byte wpCost = this.consumeCharac(this.getWpUseFromValue(), FighterCharacteristicType.WP);
        final byte chrageCost = this.consumeCharac(this.getChrageUseFromValue(), FighterCharacteristicType.CHRAGE);
        this.updateValue(apCost, mpCost, wpCost, chrageCost);
        if (mpCost == 0 && apCost == 0 && wpCost == 0) {
            this.setNotified(true);
        }
    }
    
    private byte consumeCharac(final byte baseCost, final CharacteristicType charac) {
        if (this.m_target != null && baseCost > 0 && this.m_target.hasCharacteristic(charac)) {
            final byte previousValue = (byte)this.m_target.getCharacteristicValue(charac);
            this.m_target.getCharacteristic(charac).substract(baseCost);
            return (byte)(previousValue - this.m_target.getCharacteristicValue(charac));
        }
        return 0;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
    public boolean hasDuration() {
        return false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_isForMovement = false;
        this.m_isForSpell = false;
    }
    
    public void setForMovement(final boolean forMovement) {
        this.m_isForMovement = forMovement;
    }
    
    public void setForSpell(final boolean forSpell) {
        this.m_isForSpell = forSpell;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ActionCost>() {
            @Override
            public ActionCost makeObject() {
                return new ActionCost();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Interdit", new WakfuRunningEffectParameter[0]) });
    }
}
