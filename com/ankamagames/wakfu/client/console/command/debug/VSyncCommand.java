package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.opengl.*;

public class VSyncCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final Renderer renderer = WakfuClientInstance.getInstance().getRenderer();
        renderer.requestVSync(!renderer.isVSyncEnabled());
        if (!renderer.isVSyncEnabled()) {
            manager.trace("vsync: on");
        }
        else {
            manager.trace("vsync: off");
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
