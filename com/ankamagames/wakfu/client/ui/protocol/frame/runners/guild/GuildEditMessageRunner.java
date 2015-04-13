package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;

public class GuildEditMessageRunner implements MessageRunner
{
    @Override
    public boolean run(final Message message) {
        final UIMessage msg = (UIMessage)message;
        WakfuGuildView.getInstance().modifyGuildMessageRequest(msg.getStringValue());
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 18217;
    }
}
