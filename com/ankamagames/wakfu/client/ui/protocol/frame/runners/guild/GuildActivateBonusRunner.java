package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

public class GuildActivateBonusRunner implements MessageRunner
{
    @Override
    public boolean run(final Message message) {
        final UIMessage msg = (UIMessage)message;
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(new GuildActivateBonusRequestMessage(msg.getIntValue()));
        }
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 18224;
    }
}
