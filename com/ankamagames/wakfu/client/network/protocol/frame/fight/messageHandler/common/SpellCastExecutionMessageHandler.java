package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.scriptedAction.*;
import com.ankamagames.framework.script.libraries.scriptedAction.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class SpellCastExecutionMessageHandler extends UsingFightMessageHandler<SpellCastExecutionMessage, Fight>
{
    @Override
    public boolean onMessage(final SpellCastExecutionMessage msg) {
        final CharacterInfo caster = ((Fight)this.m_concernedFight).getFighterFromId(msg.getCasterId());
        if (caster != null) {
            WeaponAnimHelper.addSpellAnimationAction(msg.getUniqueId(), msg.getActionId(), msg.getFightId(), caster);
        }
        this.addSpellAction(msg);
        return false;
    }
    
    private void addSpellAction(final SpellCastExecutionMessage msg) {
        final SpellCastAction spellCastAction = new SpellCastAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), msg.getFightId(), msg.getSerializedSpellLevel(), msg.isCriticalHit(), msg.isCriticalMiss(), msg.getCasterId(), msg.getCastPositionX(), msg.getCastPositionY(), msg.getCastPositionZ());
        final ActionGroup group = FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), spellCastAction);
        spellCastAction.addJavaFunctionsLibrary(new WakfuScriptedActionFunctionsLibrary(group), new ScriptedActionFunctionsLibrary(group));
    }
}
