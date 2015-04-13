package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

public class PartySearchCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (WakfuGameEntity.getInstance().hasFrame(UIPartySearchFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(UIPartySearchFrame.INSTANCE);
            return;
        }
        WakfuGameEntity.getInstance().pushFrame(UIPartySearchFrame.INSTANCE);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
