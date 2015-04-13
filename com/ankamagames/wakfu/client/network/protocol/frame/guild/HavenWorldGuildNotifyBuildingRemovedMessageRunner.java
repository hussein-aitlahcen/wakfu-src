package com.ankamagames.wakfu.client.network.protocol.frame.guild;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.framework.kernel.core.common.message.*;

class HavenWorldGuildNotifyBuildingRemovedMessageRunner implements MessageRunner<HavenWorldGuildNotifyBuildingRemovedMessage>
{
    @Override
    public boolean run(final HavenWorldGuildNotifyBuildingRemovedMessage msg) {
        WakfuGuildView.getInstance().notifyHavenWorldBuildngRemoved(msg.getBuildingId());
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 20098;
    }
}
