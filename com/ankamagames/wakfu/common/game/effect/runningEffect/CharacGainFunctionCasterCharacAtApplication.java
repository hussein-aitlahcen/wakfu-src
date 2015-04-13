package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacGainFunctionCasterCharacAtApplication extends CharacGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private CharacteristicType[] m_sourceCharacs;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacGainFunctionCasterCharacAtApplication.PARAMETERS_LIST_SET;
    }
    
    public CharacGainFunctionCasterCharacAtApplication() {
        super();
        this.setTriggersToExecute();
    }
    
    public CharacGainFunctionCasterCharacAtApplication(final CharacteristicType targetCharac, final CharacteristicType... sourceCharacs) {
        super(targetCharac);
        this.m_sourceCharacs = sourceCharacs;
        this.setTriggersToExecute();
    }
    
    @Override
    public CharacGainFunctionCasterCharacAtApplication newInstance() {
        CharacGainFunctionCasterCharacAtApplication re;
        try {
            re = (CharacGainFunctionCasterCharacAtApplication)CharacGainFunctionCasterCharacAtApplication.m_staticPool.borrowObject();
            re.m_pool = CharacGainFunctionCasterCharacAtApplication.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacGainFunctionCasterCharacAtApplication();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacGainFunctionCasterCharacAtApplication.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacGainFunctionCasterCharacAtApplication : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
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
    public void onCheckIn() {
        this.m_charac = null;
        this.m_sourceCharacs = null;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacGainFunctionCasterCharacAtApplication>() {
            @Override
            public CharacGainFunctionCasterCharacAtApplication makeObject() {
                return new CharacGainFunctionCasterCharacAtApplication();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Defaut", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Ratio en %", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("ratio en %", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
