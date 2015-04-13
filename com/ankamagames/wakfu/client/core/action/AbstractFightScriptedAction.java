package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractFightScriptedAction extends ScriptedAction
{
    protected static final FightLogger m_fightLogger;
    private FightInfo m_fight;
    private final int m_fightId;
    
    public AbstractFightScriptedAction(final int uniqueId, final int type, final int actionId, final int fightInfoId) {
        super(uniqueId, type, actionId);
        this.m_fightId = fightInfoId;
        final THashMap<String, Object> map = new THashMap<String, Object>(1);
        map.put("fightId", this.m_fightId);
        this.setContextVariables(map);
        this.tryToFindFight();
    }
    
    private void tryToFindFight() {
        if (this.m_fight == null) {
            this.m_fight = FightManager.getInstance().getFightById(this.m_fightId);
        }
    }
    
    protected FightInfo getFight() {
        this.tryToFindFight();
        return this.m_fight;
    }
    
    public int getFightId() {
        return this.m_fightId;
    }
    
    public boolean consernLocalPlayer() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return false;
        }
        this.tryToFindFight();
        final Fight currentFight = localPlayer.getCurrentOrObservedFight();
        return currentFight != null && this.m_fight != null && currentFight == this.m_fight;
    }
    
    public CharacterInfo getFighterById(final long characterId) {
        this.tryToFindFight();
        CharacterInfo fighter = null;
        if (this.m_fight != null) {
            fighter = this.m_fight.getFighterFromId(characterId);
        }
        if (fighter == null) {
            fighter = CharacterInfoManager.getInstance().getCharacter(characterId);
        }
        return fighter;
    }
    
    public BasicEffectAreaManager getEffectAreaManager() {
        this.tryToFindFight();
        if (this.m_fight == null) {
            AbstractFightScriptedAction.m_logger.error((Object)("combat non trouv\u00e9 : " + this.m_fightId));
            return null;
        }
        return this.m_fight.getEffectAreaManager();
    }
    
    @Override
    protected void onActionFinished() {
        super.onActionFinished();
        final CharacterInfo instigator = this.getFighterById(this.getInstigatorId());
        final CharacterInfo target = this.getFighterById(this.getTargetId());
        ScriptedActionUtils.refreshShortcutsIfNecessary(instigator, target);
    }
    
    static {
        m_fightLogger = new FightLogger();
    }
}
