package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class OsamodasSymbiotCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (WakfuGameEntity.getInstance().getLocalPlayer().getBreed() == AvatarBreed.OSAMODAS) {
            if (!WakfuGameEntity.getInstance().getFrames().contains(UIShortcutBarFrame.getInstance())) {
                WakfuGameEntity.getInstance().pushFrame(UIShortcutBarFrame.getInstance());
            }
            UIMessage.send((short)16422);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
