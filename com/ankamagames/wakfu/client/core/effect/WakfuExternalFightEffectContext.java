package com.ankamagames.wakfu.client.core.effect;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.framework.ai.dataProvider.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public class WakfuExternalFightEffectContext implements WakfuFightEffectContextInterface
{
    protected static final Logger m_logger;
    private final ExternalFightInfo m_fightInfo;
    
    public WakfuExternalFightEffectContext(final ExternalFightInfo fight) {
        super();
        this.m_fightInfo = fight;
    }
    
    @Override
    public byte getContextType() {
        return 2;
    }
    
    @Override
    public SpellCaster getSpellCaster() {
        return null;
    }
    
    @Override
    public MonsterSpawner getMonsterSpawner() {
        return null;
    }
    
    @Override
    public EffectExecutionListener getEffectExecutionListener() {
        return this.m_fightInfo;
    }
    
    @Override
    public TurnBasedTimeline getTimeline() {
        return null;
    }
    
    @Override
    public FightMap getFightMap() {
        return null;
    }
    
    @Override
    public BasicEffectAreaManager getEffectAreaManager() {
        return this.m_fightInfo.getEffectAreaManager();
    }
    
    @Override
    public AbstractEffectManager<WakfuEffect> getEffectManager() {
        return EffectManager.getInstance();
    }
    
    @Override
    public EffectUserInformationProvider getEffectUserInformationProvider() {
        return this.m_fightInfo;
    }
    
    @Override
    public TargetInformationProvider<EffectUser> getTargetInformationProvider() {
        return this.m_fightInfo;
    }
    
    @Override
    public int getFightId() {
        return this.m_fightInfo.getId();
    }
    
    @Override
    public void onEffectAreaGoesOffPlay(final AbstractEffectArea area) {
        this.m_fightInfo.onEffectAreaGoesOffPlay(area);
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuExternalFightEffectContext.class);
    }
}
