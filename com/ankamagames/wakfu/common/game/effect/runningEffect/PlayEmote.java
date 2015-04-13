package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class PlayEmote extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private boolean m_displayChatMessage;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    public static PlayEmote checkOut(final EffectContext<WakfuEffect> context, final Elements element) {
        PlayEmote re;
        try {
            re = (PlayEmote)PlayEmote.m_staticPool.borrowObject();
            re.m_pool = PlayEmote.m_staticPool;
        }
        catch (Exception e) {
            re = new PlayEmote();
            re.m_pool = null;
            re.m_isStatic = false;
            PlayEmote.m_logger.error((Object)("Erreur lors d'un checkOut sur un PlayEmote : " + e.getMessage()));
        }
        re.m_id = RunningEffectConstants.PLAY_EMOTE.getId();
        re.m_status = RunningEffectConstants.PLAY_EMOTE.getObject().getRunningEffectStatus();
        re.m_context = (EffectContext<FX>)context;
        return re;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_displayChatMessage = false;
    }
    
    @Override
    public PlayEmote newInstance() {
        PlayEmote re;
        try {
            re = (PlayEmote)PlayEmote.m_staticPool.borrowObject();
            re.m_pool = PlayEmote.m_staticPool;
        }
        catch (Exception e) {
            re = new PlayEmote();
            re.m_pool = null;
            re.m_isStatic = false;
            PlayEmote.m_logger.error((Object)("Erreur lors d'un checkOut sur un PlayEmote : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return PlayEmote.PARAMETERS_LIST_SET;
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
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, (short)0, RoundingMethod.RANDOM);
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
                this.m_displayChatMessage = (((WakfuEffect)this.m_genericEffect).getParam(1) == 0.0f);
            }
            else {
                this.m_displayChatMessage = false;
            }
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_caster != null && this.m_caster instanceof BasicCharacterInfo) {
            ((BasicCharacterInfo)this.m_caster).playEmote(this.m_value, this.m_displayChatMessage);
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<PlayEmote>() {
            @Override
            public PlayEmote makeObject() {
                return new PlayEmote();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("id de script", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("id de script + feedback", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Afficher dans le chat", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
