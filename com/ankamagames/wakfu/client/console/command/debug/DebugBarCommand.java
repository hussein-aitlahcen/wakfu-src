package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;

public class DebugBarCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuClientInstance instance = WakfuClientInstance.getInstance();
        final boolean debugBarWasVisible = instance.isDebugBarVisible();
        instance.displayDebugBar(!debugBarWasVisible);
        if (debugBarWasVisible == instance.isDebugBarVisible()) {
            if (debugBarWasVisible) {
                manager.err("Unable to hide DebugBar");
            }
            else {
                manager.err("Unable to display DebugBar");
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
