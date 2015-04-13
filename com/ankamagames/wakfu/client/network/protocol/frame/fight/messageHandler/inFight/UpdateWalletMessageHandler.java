package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class UpdateWalletMessageHandler extends UsingFightMessageHandler<WalletUpdateMessage, Fight>
{
    @Override
    public boolean onMessage(final WalletUpdateMessage msg) {
        final UpdateWalletAction action = UpdateWalletAction.checkout(TimedAction.getNextUid(), FightActionType.UPDATE_WALLET.getId(), 0, msg.getAmountOfCash());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        return false;
    }
}
