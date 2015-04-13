package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class PlayerXpModificationMessageHandler extends UsingFightMessageHandler<PlayerXpModificationMessage, Fight>
{
    @Override
    public boolean onMessage(final PlayerXpModificationMessage msg) {
        if (!msg.isFightXp()) {
            return true;
        }
        if (this.m_concernedFight != null) {
            ((Fight)this.m_concernedFight).setPlayerXpModifications(msg.getXpModifications());
        }
        final PlayerXpModificationAction playerXpModificationAction = PlayerXpModificationAction.buildFromMessage(msg);
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, playerXpModificationAction);
        final SkillOrSpellXpModificationAction[] arr$;
        final SkillOrSpellXpModificationAction[] skillOrSpellXpModificationActions = arr$ = SkillOrSpellXpModificationAction.buildFromMessage(msg);
        for (final SkillOrSpellXpModificationAction skillOrSpellXpModificationAction : arr$) {
            FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, skillOrSpellXpModificationAction);
        }
        return false;
    }
}
