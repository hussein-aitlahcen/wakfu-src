package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.*;

public abstract class AbstractFightAction extends Action
{
    private FightInfo m_fight;
    private final int m_fightId;
    
    protected AbstractFightAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
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
    
    public boolean consernLocalPlayer() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return false;
        }
        this.tryToFindFight();
        return (localPlayer.getCurrentOrObservedFight() != null || this.m_fight != null) && localPlayer.getCurrentOrObservedFight() == this.m_fight;
    }
    
    public Fight getLocalPlayerFight() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null) {
            return localPlayer.getCurrentFight();
        }
        return null;
    }
    
    public CharacterInfo getFighterById(final long characterId) {
        this.tryToFindFight();
        if (this.m_fight != null) {
            return this.m_fight.getFighterFromId(characterId);
        }
        return CharacterInfoManager.getInstance().getCharacter(characterId);
    }
    
    public BasicEffectAreaManager getEffectAreaManager() {
        this.tryToFindFight();
        if (this.m_fight == null) {
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
    public final void run() {
        try {
            this.runCore();
        }
        catch (Exception e) {
            AbstractFightAction.m_logger.error((Object)String.format("Erreur lors de l'ex\u00e9cution de la %s #%d", this.getClass().getSimpleName(), this.getActionId()), (Throwable)e);
        }
        finally {
            this.fireActionFinishedEvent();
        }
    }
    
    protected abstract void runCore();
}
