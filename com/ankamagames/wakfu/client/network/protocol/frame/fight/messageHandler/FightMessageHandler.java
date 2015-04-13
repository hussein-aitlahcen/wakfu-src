package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public interface FightMessageHandler<T extends Message, F extends FightInfo>
{
    void setConcernedFight(F p0);
    
    boolean onMessage(T p0);
    
    int getHandledMessageId();
    
    void setHandledMessageId(int p0);
}
