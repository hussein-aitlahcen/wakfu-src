package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HPGainWithFeedBack extends HPGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private Elements m_element;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPGainWithFeedBack.PARAMETERS_LIST_SET;
    }
    
    private HPGainWithFeedBack() {
        super();
    }
    
    public HPGainWithFeedBack(final Elements element) {
        super();
        this.m_element = element;
        this.setTriggersToExecute();
    }
    
    @Override
    public Elements getElement() {
        return this.m_element;
    }
    
    public static HPGainWithFeedBack checkOut(final EffectContext<WakfuEffect> context, final Elements element) {
        HPGainWithFeedBack re;
        try {
            re = (HPGainWithFeedBack)HPGainWithFeedBack.m_staticPool.borrowObject();
            re.m_pool = HPGainWithFeedBack.m_staticPool;
        }
        catch (Exception e) {
            re = new HPGainWithFeedBack();
            re.m_pool = null;
            re.m_isStatic = false;
            HPGainWithFeedBack.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPGainWithFeedBack : " + e.getMessage()));
        }
        re.m_element = element;
        re.m_id = RunningEffectConstants.HP_GAIN.getId();
        re.m_status = RunningEffectConstants.HP_GAIN.getObject().getRunningEffectStatus();
        re.m_maxExecutionCount = -1;
        re.m_context = (EffectContext<FX>)context;
        return re;
    }
    
    @Override
    public HPGainWithFeedBack newInstance() {
        HPGainWithFeedBack re;
        try {
            re = (HPGainWithFeedBack)HPGainWithFeedBack.m_staticPool.borrowObject();
            re.m_pool = HPGainWithFeedBack.m_staticPool;
        }
        catch (Exception e) {
            re = new HPGainWithFeedBack();
            re.m_pool = null;
            re.m_isStatic = false;
            HPGainWithFeedBack.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPGainWithFeedBack : " + e.getMessage()));
        }
        re.m_element = this.m_element;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(1);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_caster != null && this.m_caster.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.m_value = this.m_caster.getCharacteristic(FighterCharacteristicType.HP).substract(this.m_value);
        }
        super.executeOverride(linkedRE, trigger);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<HPGainWithFeedBack>() {
            @Override
            public HPGainWithFeedBack makeObject() {
                return new HPGainWithFeedBack((HPGainWithFeedBack$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("gain de PdV", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("gain de PdV", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("+", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
