package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class REGExecutionCountFunctionAlliesCount extends REGExecutionCountParameterized
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return REGExecutionCountFunctionAlliesCount.PARAMETERS_LIST_SET;
    }
    
    public REGExecutionCountFunctionAlliesCount() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public REGExecutionCountFunctionAlliesCount newInstance() {
        REGExecutionCountFunctionAlliesCount re;
        try {
            re = (REGExecutionCountFunctionAlliesCount)REGExecutionCountFunctionAlliesCount.m_staticPool.borrowObject();
            re.m_pool = REGExecutionCountFunctionAlliesCount.m_staticPool;
        }
        catch (Exception e) {
            re = new REGExecutionCountFunctionAlliesCount();
            re.m_pool = null;
            re.m_isStatic = false;
            REGExecutionCountFunctionAlliesCount.m_logger.error((Object)("Erreur lors d'un checkOut sur un REGExecutionCountFunctionCharac : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected int computeExecutionsCountParameter(final RunningEffect triggerRE) {
        if (this.m_caster == null || !(this.m_caster instanceof FightEffectUser)) {
            return 0;
        }
        final EffectContext context = this.getContext();
        if (context == null || !(context instanceof WakfuFightEffectContext)) {
            return 0;
        }
        final WakfuFightEffectContext fightContext = (WakfuFightEffectContext)context;
        final AbstractFight fight = fightContext.getFight();
        if (fight == null) {
            return 0;
        }
        final Collection fightersInTeam = fight.getFightersInTeam(((FightEffectUser)this.m_caster).getTeamId());
        return Math.max(0, fightersInTeam.size() - 1);
    }
    
    @Override
    protected boolean computeShouldStopOnEffectNotExecuted(final RunningEffect triggerRE) {
        return false;
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
        m_staticPool = new MonitoredPool(new ObjectFactory<REGExecutionCountFunctionAlliesCount>() {
            @Override
            public REGExecutionCountFunctionAlliesCount makeObject() {
                return new REGExecutionCountFunctionAlliesCount();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
