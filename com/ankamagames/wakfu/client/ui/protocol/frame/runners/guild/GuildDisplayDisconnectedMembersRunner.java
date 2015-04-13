package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;

public class GuildDisplayDisconnectedMembersRunner implements MessageRunner
{
    @Override
    public boolean run(final Message message) {
        final UIMessage msg = (UIMessage)message;
        WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.GUILD_DISPLAY_DISCONNECTED_MEMBERS_KEY, msg.getBooleanValue());
        WakfuGuildView.getInstance().setDisplayDisconnectedMembers(msg.getBooleanValue());
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 18209;
    }
}
