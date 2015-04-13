package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.actionRequest.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.nation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class NationActionRequestDispatcher
{
    public static final NationActionRequestDispatcher INSTANCE;
    
    public void send(final NationActionRequest request) {
        final NationActionRequestMessage msg = new NationActionRequestMessage();
        msg.setRequest(request);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    static {
        INSTANCE = new NationActionRequestDispatcher();
    }
}
