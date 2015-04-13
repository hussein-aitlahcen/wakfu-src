package com.ankamagames.wakfu.client.console.command.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class EndPlacementAndTurnCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (WakfuGameEntity.getInstance().hasFrame(UIFightPlacementFrame.getInstance())) {
            UIMessage.send((short)18007);
        }
        else if (WakfuGameEntity.getInstance().hasFrame(UIFightEndTurnFrame.getInstance())) {
            UIMessage.send((short)18001);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
