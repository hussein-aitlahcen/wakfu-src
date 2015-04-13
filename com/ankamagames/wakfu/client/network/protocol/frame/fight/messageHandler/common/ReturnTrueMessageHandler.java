package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

final class ReturnTrueMessageHandler implements FightMessageHandler<Message, FightInfo>
{
    @Override
    public void setConcernedFight(final FightInfo fight) {
    }
    
    @Override
    public boolean onMessage(final Message msg) {
        return true;
    }
    
    @Override
    public int getHandledMessageId() {
        return 0;
    }
    
    @Override
    public void setHandledMessageId(final int messageId) {
    }
}
