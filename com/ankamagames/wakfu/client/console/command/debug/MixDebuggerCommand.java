package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;

public class MixDebuggerCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final UIMixDebugger debugger = UIMixDebugger.getInstance();
        if (WakfuGameEntity.getInstance().hasFrame(debugger)) {
            WakfuGameEntity.getInstance().removeFrame(debugger);
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(debugger);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
