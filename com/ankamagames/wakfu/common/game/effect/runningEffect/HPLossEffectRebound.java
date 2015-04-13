package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HPLossEffectRebound extends AbstractEffectRebound
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private Elements m_elements;
    private HPLoss.ComputeMode m_mode;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPLossEffectRebound.PARAMETERS_LIST_SET;
    }
    
    public HPLossEffectRebound() {
        super();
    }
    
    public HPLossEffectRebound(final Elements element, final HPLoss.ComputeMode mode) {
        super();
        this.m_elements = element;
        this.m_mode = mode;
    }
    
    @Override
    public HPLossEffectRebound getBorrowedObjectFromPool() {
        HPLossEffectRebound re;
        try {
            re = (HPLossEffectRebound)HPLossEffectRebound.m_staticPool.borrowObject();
            re.m_pool = HPLossEffectRebound.m_staticPool;
        }
        catch (Exception e) {
            re = new HPLossEffectRebound();
            re.m_pool = null;
            HPLossEffectRebound.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPLeech : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public HPLossEffectRebound newInstance() {
        final HPLossEffectRebound re = (HPLossEffectRebound)super.newInstance();
        re.m_elements = this.m_elements;
        re.m_mode = this.m_mode;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup == null || effectGroup.getEffect(0) == null) {
            throw new IllegalArgumentException("Un effet de perte de pdv avec rebond doit \u00eatre d\u00e9fini avec un sous effet qui sert de Generic Effect \u00e0 la perte de pdv");
        }
        final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_elements, this.m_mode, this.m_value, this.m_target);
        hpLoss.setCaster(this.m_caster);
        if (this.m_target != null) {
            final Point3 targetPos = this.m_target.getPosition();
            hpLoss.setTargetCell(targetPos.getX(), targetPos.getY(), targetPos.getZ());
        }
        ((RunningEffect<FX, WakfuEffectContainer>)hpLoss).setEffectContainer(((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer());
        hpLoss.setTriggersToExecute();
        hpLoss.addCustomTriggersToExecute(this.getTriggersToExecute());
        if (this.m_genericEffect != null) {
            hpLoss.addCustomTriggersToExecute(((WakfuEffect)this.m_genericEffect).getExecutionTriggersAdditionnal());
        }
        hpLoss.forceInstant();
        final WakfuEffect hpLossGenericEffect = effectGroup.getEffect(0);
        ((RunningEffect<WakfuEffect, EC>)hpLoss).setGenericEffect(hpLossGenericEffect);
        hpLoss.trigger((byte)1);
        hpLoss.computeBaseValue();
        hpLoss.computeModificator(hpLoss.defaultCondition(), this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).checkFlags(1L), this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).isAffectedByLocalisation());
        hpLoss.askForExecution();
        super.executeOverride(linkedRE, trigger);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_reboundCount == 0 && this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
            this.m_reboundReduction = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
            this.m_maxCellRange = ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM);
            this.m_dispersionMax = (byte)((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.RANDOM);
            this.m_originalValue = this.m_value;
            this.m_alreadyTargetedTargetIds = new HashSet<Long>();
            this.m_isDiffused = (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 5 && ((WakfuEffect)this.m_genericEffect).getParam(4, level, RoundingMethod.RANDOM) == 1);
        }
        super.effectiveComputeValue(triggerRE);
    }
    
    @Override
    protected boolean checkTargetValidity(final FightEffectUser caster, final FightEffectUser target) {
        return caster != null && target != null && caster.getTeamId() != target.getTeamId();
    }
    
    @Override
    public Elements getElement() {
        return this.m_elements;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HPLossEffectRebound>() {
            @Override
            public HPLossEffectRebound makeObject() {
                return new HPLossEffectRebound();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Rebond", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de l'attaque", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("R\u00e9duction \u00e0 chaque rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Port\u00e9e maximum d'un rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Nombre de Dispersion", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Rebond", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de l'attaque", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("R\u00e9duction \u00e0 chaque rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Port\u00e9e maximum d'un rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Nombre de Dispersion", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Type Diffusion (0 : non, 1 : oui) par d\u00e9faut 0", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
