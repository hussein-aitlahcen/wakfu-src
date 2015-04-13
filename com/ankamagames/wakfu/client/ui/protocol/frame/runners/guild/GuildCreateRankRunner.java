package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;

public class GuildCreateRankRunner implements MessageRunner
{
    @Override
    public boolean run(final Message message) {
        WakfuGuildView.getInstance().createNewRank();
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 18211;
    }
}
