package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

public class GuildBannerCreatorCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (WakfuGameEntity.getInstance().hasFrame(UIGuildCreatorFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIGuildCreatorFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIGuildCreatorFrame.getInstance());
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
