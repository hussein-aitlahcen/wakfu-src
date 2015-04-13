package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.*;

public abstract class AbstractFightTimedAction extends TimedAction
{
    protected static final FightLogger m_fightLogger;
    private FightInfo m_fight;
    private final int m_fightId;
    
    public AbstractFightTimedAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId);
        this.m_fightId = fightId;
    }
    
    private void tryToFindFight() {
        if (this.m_fight == null) {
            this.m_fight = FightManager.getInstance().getFightById(this.m_fightId);
        }
    }
    
    public FightInfo getFight() {
        this.tryToFindFight();
        return this.m_fight;
    }
    
    protected int getFightId() {
        return this.m_fightId;
    }
    
    public boolean consernLocalPlayer() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return false;
        }
        this.tryToFindFight();
        return localPlayer.getCurrentFight() != null && this.m_fight != null && localPlayer.getCurrentFight() == this.m_fight;
    }
    
    public CharacterInfo getFighterById(final long characterId) {
        this.tryToFindFight();
        if (this.m_fight != null) {
            return this.m_fight.getFighterFromId(characterId);
        }
        return null;
    }
    
    public BasicEffectAreaManager getEffectAreaManager() {
        this.tryToFindFight();
        if (this.m_fight == null) {
            AbstractFightTimedAction.m_logger.error((Object)("combat non trouv\u00e9 : " + this.m_fightId));
            return null;
        }
        return this.m_fight.getEffectAreaManager();
    }
    
    public EffectContext getEffectContext() {
        this.tryToFindFight();
        if (this.m_fight != null) {
            return this.m_fight.getContext();
        }
        return WakfuInstanceEffectContext.getInstance();
    }
    
    @Override
    protected void onActionFinished() {
        final CharacterInfo instigator = this.getFighterById(this.getInstigatorId());
        final CharacterInfo target = this.getFighterById(this.getTargetId());
        ScriptedActionUtils.refreshShortcutsIfNecessary(instigator, target);
    }
    
    static {
        m_fightLogger = new FightLogger();
    }
}
