package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HPGainEffectRebound extends AbstractEffectRebound
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private Elements m_element;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPGainEffectRebound.PARAMETERS_LIST_SET;
    }
    
    private HPGainEffectRebound() {
        super();
    }
    
    public HPGainEffectRebound(final Elements element) {
        super();
        this.m_element = element;
    }
    
    @Override
    public HPGainEffectRebound getBorrowedObjectFromPool() {
        HPGainEffectRebound re;
        try {
            re = (HPGainEffectRebound)HPGainEffectRebound.m_staticPool.borrowObject();
            re.m_pool = HPGainEffectRebound.m_staticPool;
        }
        catch (Exception e) {
            re = new HPGainEffectRebound();
            re.m_pool = null;
            HPGainEffectRebound.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPLeech : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public HPGainEffectRebound newInstance() {
        final HPGainEffectRebound res = (HPGainEffectRebound)super.newInstance();
        res.m_element = this.m_element;
        return res;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final HPGain hpGain = HPGain.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_element);
        hpGain.setCaster(this.m_caster);
        hpGain.forceValue(this.m_value);
        hpGain.setTarget(this.m_target);
        ((RunningEffect<WakfuEffect, EC>)hpGain).setGenericEffect(((RunningEffect<WakfuEffect, EC>)this).getGenericEffect());
        hpGain.setParamsToDefault();
        hpGain.modifyValueWithModificatorIfNecessary();
        hpGain.forceInstant();
        hpGain.setTriggersToExecute();
        hpGain.askForExecution();
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
        return caster != null && target != null && caster.getTeamId() == target.getTeamId();
    }
    
    @Override
    public Elements getElement() {
        return this.m_element;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HPGainEffectRebound>() {
            @Override
            public HPGainEffectRebound makeObject() {
                return new HPGainEffectRebound((HPGainEffectRebound$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Rebond", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de l'attaque", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("R\u00e9duction \u00e0 chaque rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Port\u00e9e maximum d'un rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Nombre de Dispersion", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Rebond, type diffusion", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de l'attaque", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("R\u00e9duction \u00e0 chaque rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Port\u00e9e maximum d'un rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Nombre de Dispersion", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Type Diffusion (0 : non, 1 : oui) par d\u00e9faut 0", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Rebond avec \u00e9l\u00e9ment", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de l'attaque", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("R\u00e9duction \u00e0 chaque rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Port\u00e9e maximum d'un rebond", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Nombre de Dispersion", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Type Diffusion (0 : non, 1 : oui) par d\u00e9faut 0", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
