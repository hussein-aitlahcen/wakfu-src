package com.ankamagames.wakfu.client.network.protocol.frame.guild;

import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.wakfu.common.game.guild.change.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.guild.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;

class GuildChangeMessageRunner implements MessageRunner<GuildChangeMessage>
{
    @Override
    public boolean run(final GuildChangeMessage msg) {
        final Guild guild = WakfuGuildView.getInstance().getGuild();
        if (guild == null) {
            NetGuildFrame.m_logger.error((Object)"On re\u00e7oit un update de guilde alors qu'on en poss\u00e8de pas !");
            return false;
        }
        final GuildController controller = new GuildControllerViewNotifier(guild);
        final Iterator<GuildChange> it = msg.changesIterator();
        while (it.hasNext()) {
            it.next().compute(controller);
        }
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 20050;
    }
}
