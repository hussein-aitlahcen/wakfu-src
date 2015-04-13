package com.ankamagames.wakfu.common.game.effect;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.ai.dataProvider.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

public abstract class AbstractWakfuInstanceEffectContext implements EffectContext<WakfuEffect>
{
    protected static final Logger m_logger;
    
    @Override
    public byte getContextType() {
        return 0;
    }
    
    @Override
    public FightMap getFightMap() {
        return null;
    }
    
    @Override
    public BasicEffectAreaManager getEffectAreaManager() {
        return null;
    }
    
    @Override
    public abstract EffectUserInformationProvider getEffectUserInformationProvider();
    
    @Override
    public TargetInformationProvider<EffectUser> getTargetInformationProvider() {
        return null;
    }
    
    @Override
    public abstract EffectExecutionListener getEffectExecutionListener();
    
    @Override
    public AbstractEffectManager<WakfuEffect> getEffectManager() {
        return null;
    }
    
    public void release() {
    }
    
    @Override
    public TurnBasedTimeline getTimeline() {
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractWakfuInstanceEffectContext.class);
    }
}
