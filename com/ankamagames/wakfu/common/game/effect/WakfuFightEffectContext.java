package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.wakfu.common.datas.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.ai.dataProvider.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class WakfuFightEffectContext<F extends BasicCharacterInfo> implements WakfuFightEffectContextInterface
{
    protected static final Logger m_logger;
    protected AbstractFight<F> m_fight;
    private final AbstractEffectManager<WakfuEffect> m_effectManager;
    
    public WakfuFightEffectContext(final AbstractFight<F> fight, final AbstractEffectManager<WakfuEffect> effectManager) {
        super();
        this.m_fight = fight;
        this.m_effectManager = effectManager;
    }
    
    @Override
    public byte getContextType() {
        return 1;
    }
    
    @Override
    public EffectExecutionListener getEffectExecutionListener() {
        return this.m_fight.getEffectExecutionListener();
    }
    
    @Override
    public AbstractEffectManager<WakfuEffect> getEffectManager() {
        return this.m_effectManager;
    }
    
    @Override
    public TargetInformationProvider<EffectUser> getTargetInformationProvider() {
        return this.m_fight;
    }
    
    @Override
    public TurnBasedTimeline getTimeline() {
        return (this.m_fight != null) ? this.m_fight.getTimeline() : null;
    }
    
    @Override
    public FightMap getFightMap() {
        return (this.m_fight != null) ? this.m_fight.getFightMap() : null;
    }
    
    @Override
    public EffectUserInformationProvider getEffectUserInformationProvider() {
        return this.m_fight;
    }
    
    @Override
    public BasicEffectAreaManager getEffectAreaManager() {
        return (this.m_fight != null) ? this.m_fight.getEffectAreaManager() : null;
    }
    
    public AbstractFight<F> getFight() {
        return this.m_fight;
    }
    
    @Override
    public int getFightId() {
        return this.m_fight.getId();
    }
    
    @Override
    public void onEffectAreaGoesOffPlay(final AbstractEffectArea area) {
        this.m_fight.onEffectAreaGoesOffPlay(area);
    }
    
    public void onBlockAchieved(final EffectUser target) {
        this.m_fight.onBlockAchieved(target);
    }
    
    @Override
    public SpellCaster getSpellCaster() {
        return this.m_fight.getSpellCaster();
    }
    
    @Override
    public MonsterSpawner getMonsterSpawner() {
        return this.m_fight.getMonsterSpawner();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuFightEffectContext.class);
    }
}
