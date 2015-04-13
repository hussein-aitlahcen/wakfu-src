package com.ankamagames.wakfu.client.network.protocol.frame.guild;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.framework.kernel.core.common.message.*;

class HavenWorldGuildNotifyBuildingEvolvedMessageRunner implements MessageRunner<HavenWorldGuildNotifyBuildingEvolvedMessage>
{
    @Override
    public boolean run(final HavenWorldGuildNotifyBuildingEvolvedMessage msg) {
        WakfuGuildView.getInstance().notifyHavenWorldBuildngEvolved(msg.getBuildingId(), msg.getBuildingIdTo());
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 20073;
    }
}
