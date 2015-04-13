package com.ankamagames.wakfu.client.core.game.fight.actionsOperations;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.scriptedAction.*;
import com.ankamagames.framework.script.libraries.scriptedAction.*;
import com.ankamagames.framework.script.action.*;

public final class SpellCastOperations
{
    private static final Logger m_logger;
    private final FightInfo m_fight;
    private final SpellCastExecutionMessage msg;
    
    public SpellCastOperations(final FightInfo fight, final SpellCastExecutionMessage msg) {
        super();
        this.m_fight = fight;
        this.msg = msg;
    }
    
    public void execute() {
        if (this.m_fight == null) {
            SpellCastOperations.m_logger.error((Object)("[FIGHT] On veut executer un sort sur un combat inconnu " + this.msg.getFightId()));
            return;
        }
        final CharacterInfo caster = this.m_fight.getFighterFromId(this.msg.getCasterId());
        if (caster == null) {
            return;
        }
        final CharacterActor casterActor = caster.getActor();
        casterActor.clearActiveParticleSystem();
        final SpellLevel spellLevel = this.extractSpellLevel(caster);
        if (spellLevel == null) {
            return;
        }
        WeaponAnimHelper.addSpellAnimationAction(this.msg.getUniqueId(), this.msg.getActionId(), this.msg.getFightId(), caster);
        this.addSpellAction(this.msg.getSerializedSpellLevel());
    }
    
    private SpellLevel extractSpellLevel(final CharacterInfo caster) {
        final SpellInventory<? extends AbstractSpellLevel> spellInventory = caster.getSpellInventory();
        SpellLevel spellLevel;
        if (spellInventory != null && spellInventory.getContentProvider() != null) {
            spellLevel = spellInventory.getContentProvider().unSerializeContent(this.msg.getSerializedSpellLevel());
        }
        else {
            final SpellLevelProvider spellLevelProvider = new SpellLevelProvider(caster);
            spellLevel = spellLevelProvider.unSerializeContent(this.msg.getSerializedSpellLevel());
        }
        return spellLevel;
    }
    
    private void addSpellAction(final RawSpellLevel spellLevel) {
        final SpellCastAction spellCastAction = new SpellCastAction(this.msg.getUniqueId(), this.msg.getFightActionType().getId(), this.msg.getActionId(), this.msg.getFightId(), spellLevel, this.msg.isCriticalHit(), this.msg.isCriticalMiss(), this.msg.getCasterId(), this.msg.getCastPositionX(), this.msg.getCastPositionY(), this.msg.getCastPositionZ());
        final ActionGroup group = FightActionGroupManager.getInstance().addActionToPendingGroup(this.msg.getFightId(), spellCastAction);
        spellCastAction.addJavaFunctionsLibrary(new WakfuScriptedActionFunctionsLibrary(group), new ScriptedActionFunctionsLibrary(group));
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellCastOperations.class);
    }
}
