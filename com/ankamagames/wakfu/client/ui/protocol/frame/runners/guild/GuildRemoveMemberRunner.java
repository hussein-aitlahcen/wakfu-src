package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.xulor2.core.messagebox.*;

public class GuildRemoveMemberRunner implements MessageRunner
{
    @Override
    public boolean run(final Message message) {
        final UIMessage msg = (UIMessage)message;
        final GuildMember member = WakfuGuildView.getInstance().getMember(msg.getLongValue());
        if (member == null) {
            return false;
        }
        final String guildExcludeMessage = WakfuTranslator.getInstance().getString("guild.excludeMember.validate", member.getName());
        final MessageBoxControler controler = Xulor.getInstance().msgBox(guildExcludeMessage, 6L, 102, 3);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type != 2) {
                    return;
                }
                WakfuGuildView.getInstance().requestRemoveById(member.getId());
            }
        });
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 18207;
    }
}
