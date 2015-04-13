package com.ankamagames.wakfu.client.core.effect;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.ai.dataProvider.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;

public class WakfuInstanceEffectContext extends AbstractWakfuInstanceEffectContext
{
    private final InstanceEffectExecutionListener m_effectExecutionListener;
    private static final WakfuInstanceEffectContext m_instance;
    
    private WakfuInstanceEffectContext() {
        super();
        this.m_effectExecutionListener = new InstanceEffectExecutionListener();
    }
    
    public static WakfuInstanceEffectContext getInstance() {
        return WakfuInstanceEffectContext.m_instance;
    }
    
    @Override
    public EffectExecutionListener getEffectExecutionListener() {
        return this.m_effectExecutionListener;
    }
    
    @Override
    public EffectUserInformationProvider getEffectUserInformationProvider() {
        return CharacterInfoManager.getInstance();
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
    public TargetInformationProvider<EffectUser> getTargetInformationProvider() {
        return (TargetInformationProvider<EffectUser>)CharacterInfoManager.getInstance();
    }
    
    @Override
    public AbstractEffectManager<WakfuEffect> getEffectManager() {
        return EffectManager.getInstance();
    }
    
    static {
        m_instance = new WakfuInstanceEffectContext();
    }
}
