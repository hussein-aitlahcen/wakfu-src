package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private CharacteristicType[] m_sourceCharacs;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication.PARAMETERS_LIST_SET;
    }
    
    public RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication() {
        super();
        this.setTriggersToExecute();
    }
    
    public RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication(final CharacteristicType... sourceCharacs) {
        super();
        this.m_sourceCharacs = sourceCharacs;
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication newInstance() {
        RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication re;
        try {
            re = (RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication)RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacBuffFunctionCasterCharacAtApplication : " + e.getMessage()));
        }
        re.m_sourceCharacs = this.m_sourceCharacs;
        return re;
    }
    
    @Override
    public void onApplication() {
        if (this.m_caster != null && this.isValueComputationEnabled()) {
            for (int i = 0; i < this.m_sourceCharacs.length; ++i) {
                final CharacteristicType sourceCharac = this.m_sourceCharacs[i];
                if (this.m_caster.hasCharacteristic(sourceCharac)) {
                    this.m_value += this.m_caster.getCharacteristicValue(sourceCharac);
                }
            }
            if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() > 0) {
                final int ratio = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                this.m_value = this.m_value * ratio / 100;
            }
        }
        super.onApplication();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_genericEffect == null) {
            this.setNotified();
            return;
        }
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup == null) {
            RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication.m_logger.error((Object)("Groupe d'effet inconnu" + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            this.setNotified();
            return;
        }
        final WakfuEffectExecutionParameters params = this.getExecutionParameters((WakfuRunningEffect)linkedRE, true);
        params.setForcedValue(this.m_value);
        if (this.hasProperty(RunningEffectPropertyType.TRANSMIT_LEVEL_TO_CHILDREN) && params.getForcedLevel() == -1) {
            params.setForcedLevel(this.getContainerLevel());
        }
        for (final WakfuEffect effect : effectGroup) {
            try {
                effect.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.getCaster(), params, false);
            }
            catch (Exception e) {
                RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        params.release();
    }
    
    private WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(disableProbabilityComputation, false, linkedRE);
        params.setResetLimitedApplyCount(false);
        return params;
    }
    
    @Override
    public void onCheckIn() {
        this.m_sourceCharacs = null;
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication>() {
            @Override
            public RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication makeObject() {
                return new RunningEffectGroupWithSubEffectValueFunctionCasterCharacAtApplication();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Defaut", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Ratio en %", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("ratio en %", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
