package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ExchangePosition extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private AreaOccupationComputer m_casterAreaOccupationComputer;
    private AreaOccupationComputer m_targetAreaOccupationComputer;
    private int m_casterX;
    private int m_casterY;
    private short m_casterZ;
    private int m_targetX;
    private int m_targetY;
    private short m_targetZ;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ExchangePosition.PARAMETERS_LIST_SET;
    }
    
    public ExchangePosition() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(20) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(ExchangePosition.this.m_casterX);
                buffer.putInt(ExchangePosition.this.m_casterY);
                buffer.putShort(ExchangePosition.this.m_casterZ);
                buffer.putInt(ExchangePosition.this.m_targetX);
                buffer.putInt(ExchangePosition.this.m_targetY);
                buffer.putShort(ExchangePosition.this.m_targetZ);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                ExchangePosition.this.m_casterX = buffer.getInt();
                ExchangePosition.this.m_casterY = buffer.getInt();
                ExchangePosition.this.m_casterZ = buffer.getShort();
                ExchangePosition.this.m_targetX = buffer.getInt();
                ExchangePosition.this.m_targetY = buffer.getInt();
                ExchangePosition.this.m_targetZ = buffer.getShort();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public final void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(184);
    }
    
    @Override
    public ExchangePosition newInstance() {
        ExchangePosition re;
        try {
            re = (ExchangePosition)ExchangePosition.m_staticPool.borrowObject();
            re.m_pool = ExchangePosition.m_staticPool;
        }
        catch (Exception e) {
            re = new ExchangePosition();
            re.m_pool = null;
            re.m_isStatic = false;
            ExchangePosition.m_logger.error((Object)("Erreur lors d'un checkOut sur un Push : " + e.getMessage()));
        }
        re.m_casterX = this.m_casterX;
        re.m_casterY = this.m_casterY;
        re.m_casterZ = this.m_casterZ;
        re.m_targetX = this.m_targetX;
        re.m_targetY = this.m_targetY;
        re.m_targetZ = this.m_targetZ;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        boolean mustBeExecuted = true;
        if (this.m_caster instanceof CarryTarget) {
            if (((CarryTarget)this.m_caster).isCarried()) {
                mustBeExecuted = false;
            }
            if (this.m_caster.isOffPlay() || this.m_caster.isOutOfPlay()) {
                mustBeExecuted = false;
            }
        }
        if (this.m_target instanceof CarryTarget && ((CarryTarget)this.m_target).isCarried()) {
            mustBeExecuted = false;
        }
        final EffectUser target = this.m_target;
        final EffectUser caster = this.m_caster;
        boolean bypassStabilized = false;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 1) {
            bypassStabilized = (((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (!bypassStabilized) {
            if (this.m_target != null && (this.m_target.isActiveProperty(FightPropertyType.STABILIZED) || this.m_target.isActiveProperty(FightPropertyType.CANT_TRANSPOSE))) {
                this.setNotified(true);
                return;
            }
            if (this.m_caster != null && (this.m_caster.isActiveProperty(FightPropertyType.STABILIZED) || this.m_caster.isActiveProperty(FightPropertyType.CANT_TRANSPOSE))) {
                this.setNotified(true);
                return;
            }
        }
        if (mustBeExecuted && caster != null && target != null && this.m_target instanceof MovementEffectUser) {
            if (this.isValueComputationEnabled()) {
                this.m_casterX = caster.getWorldCellX();
                this.m_casterY = caster.getWorldCellY();
                this.m_casterZ = caster.getWorldCellAltitude();
                this.m_targetX = target.getWorldCellX();
                this.m_targetY = target.getWorldCellY();
                this.m_targetZ = target.getWorldCellAltitude();
            }
            if (this.isValueComputationEnabled() && target instanceof BasicCharacterInfo && caster instanceof BasicCharacterInfo) {
                this.initializeAreaOccupationComputers((BasicCharacterInfo)target, (BasicCharacterInfo)caster);
            }
            caster.teleport(this.m_targetX, this.m_targetY, this.m_targetZ);
            target.teleport(this.m_casterX, this.m_casterY, this.m_casterZ);
            final RunningEffect triggeringEffect = this.getTriggeringEffect(linkedRE);
            if (triggeringEffect != null) {
                if (this.mustStoreOnCaster()) {
                    triggeringEffect.setTarget(target);
                }
                else {
                    triggeringEffect.setTarget(caster);
                }
            }
            this.notifyExecution(triggeringEffect, trigger);
            if (this.m_context.getEffectAreaManager() != null && this.isValueComputationEnabled()) {
                this.computeAreaModifications(target, caster);
            }
        }
        else {
            this.setNotified(true);
        }
    }
    
    private void initializeAreaOccupationComputers(final BasicCharacterInfo target, final BasicCharacterInfo caster) {
        final AbstractFight<BasicCharacterInfo> fight = ((WakfuFightEffectContext)this.m_context).getFight();
        (this.m_casterAreaOccupationComputer = new AreaOccupationComputer(fight, caster, caster.getPosition())).setInitialState(caster.getPosition());
        (this.m_targetAreaOccupationComputer = new AreaOccupationComputer(fight, target, target.getPosition())).setInitialState(target.getPosition());
    }
    
    private void computeAreaModifications(final EffectUser target, final EffectUser caster) {
        if (this.m_casterAreaOccupationComputer == null || this.m_targetAreaOccupationComputer == null) {
            return;
        }
        this.m_casterAreaOccupationComputer.setMoverDestinationCell(caster.getPosition());
        this.m_casterAreaOccupationComputer.computeAreaModificationsOnMove();
        if (this.m_casterAreaOccupationComputer.willTriggerSomething()) {
            this.m_casterAreaOccupationComputer.triggerAreaEffects();
        }
        this.m_targetAreaOccupationComputer.setMoverDestinationCell(target.getPosition());
        this.m_targetAreaOccupationComputer.computeAreaModificationsOnMove();
        if (this.m_targetAreaOccupationComputer.willTriggerSomething()) {
            this.m_targetAreaOccupationComputer.triggerAreaEffects();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
    public void onCheckIn() {
        this.m_casterAreaOccupationComputer = null;
        this.m_targetAreaOccupationComputer = null;
        super.onCheckIn();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    public int getCasterX() {
        return this.m_casterX;
    }
    
    public int getCasterY() {
        return this.m_casterY;
    }
    
    public short getCasterZ() {
        return this.m_casterZ;
    }
    
    public int getTargetX() {
        return this.m_targetX;
    }
    
    public int getTargetY() {
        return this.m_targetY;
    }
    
    public short getTargetZ() {
        return this.m_targetZ;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ExchangePosition>() {
            @Override
            public ExchangePosition makeObject() {
                return new ExchangePosition();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Echange de position standard", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Bypass stabilisation", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Bypass stabilisation (0 = false, 1 = true)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
