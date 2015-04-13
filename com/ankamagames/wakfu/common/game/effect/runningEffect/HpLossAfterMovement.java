package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossAfterMovement extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private Elements m_element;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossAfterMovement.PARAMETERS_LIST_SET;
    }
    
    public HpLossAfterMovement() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HpLossAfterMovement newInstance() {
        HpLossAfterMovement re;
        try {
            re = (HpLossAfterMovement)HpLossAfterMovement.m_staticPool.borrowObject();
            re.m_pool = HpLossAfterMovement.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossAfterMovement();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossAfterMovement.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossAfterMovement : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = 0;
        final RunningEffect trigger = (triggerRE != null) ? triggerRE : ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect();
        if (trigger == null) {
            HpLossAfterMovement.m_logger.error((Object)"Cet effet ne peut etre utilis\u00e9 que dans le cadre d'un declenchement");
            return;
        }
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 3) {
            HpLossAfterMovement.m_logger.error((Object)"Pas le bon nombre de param");
            return;
        }
        final int dmgPerCell = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int elemId = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        int maxCellsCount = Integer.MAX_VALUE;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 3) {
            maxCellsCount = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        this.m_element = Elements.getElementFromId((byte)elemId);
        Point3 startCell = null;
        Point3 arrivalCell = null;
        if (trigger instanceof MovementEffect) {
            final MovementEffect movement = (MovementEffect)trigger;
            startCell = movement.getStartCell();
            arrivalCell = movement.getArrivalCell();
        }
        else if (trigger instanceof ExchangePosition) {
            final ExchangePosition exchange = (ExchangePosition)trigger;
            final EffectUser caster = exchange.getCaster();
            if (caster == null) {
                return;
            }
            if (caster.getWorldCellX() == exchange.getCasterX() && caster.getWorldY() == exchange.getCasterY()) {
                return;
            }
            startCell = new Point3(exchange.getCasterX(), exchange.getCasterY(), exchange.getCasterZ());
            arrivalCell = new Point3(exchange.getTargetX(), exchange.getTargetY(), exchange.getTargetZ());
        }
        else if (trigger instanceof Teleport) {
            final Teleport teleport = (Teleport)trigger;
            startCell = teleport.getStartCell();
            final EffectUser target = teleport.getCharacterToTeleport();
            if (target != null) {
                arrivalCell = new Point3(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
            }
        }
        else if (this.isThrowEffect(trigger)) {
            startCell = trigger.getCaster().getPosition();
            final EffectUser target2 = trigger.getTarget();
            if (target2 != null) {
                arrivalCell = new Point3(target2.getWorldCellX(), target2.getWorldCellY(), target2.getWorldCellAltitude());
            }
        }
        if (startCell == null || arrivalCell == null) {
            return;
        }
        int distance = startCell.getDistance(arrivalCell);
        distance = Math.min(distance, maxCellsCount);
        this.m_value = dmgPerCell * distance;
    }
    
    private boolean isThrowEffect(final RunningEffect trigger) {
        return trigger.getTriggersToExecute().get(2158);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_value == 0) {
            this.setNotified();
            return;
        }
        this.notifyExecution(triggerRE, trigger);
        final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_element, HPLoss.ComputeMode.CLASSIC, this.m_value, this.m_target);
        hpLoss.setCaster(this.m_caster);
        hpLoss.disableValueComputation();
        ((RunningEffect<DefaultFightInstantEffectWithChatNotif, EC>)hpLoss).setGenericEffect(DefaultFightInstantEffectWithChatNotif.getInstance());
        hpLoss.trigger((byte)1);
        hpLoss.computeModificatorWithDefaults();
        hpLoss.trigger((byte)2);
        hpLoss.execute(null, false);
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
    public void onCheckIn() {
        this.m_element = null;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossAfterMovement>() {
            @Override
            public HpLossAfterMovement makeObject() {
                return new HpLossAfterMovement();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("D\u00e9gats/case de deplacement ", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Dmg/case", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("D\u00e9gats/case de deplacement ", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Dmg/case", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Max de cases", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
