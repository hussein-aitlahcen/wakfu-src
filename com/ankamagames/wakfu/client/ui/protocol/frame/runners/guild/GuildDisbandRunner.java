package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.messagebox.*;

public class GuildDisbandRunner implements MessageRunner
{
    @Override
    public boolean run(final Message message) {
        final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("guild.disband.validate"), 6L, 102, 3);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type != 2) {
                    return;
                }
                WakfuGuildView.getInstance().disband();
                WakfuGameEntity.getInstance().removeFrame(UIGuildManagementFrame.getInstance());
            }
        });
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 18210;
    }
}
