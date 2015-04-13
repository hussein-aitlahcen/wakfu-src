package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class EntityStatusCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final StringBuilder builder = new StringBuilder("Status de WakfuGameEntity :");
        try {
            builder.append('\n').append("Connect\u00e9 : ").append(WakfuGameEntity.getInstance().getNetworkEntity().isConnected());
        }
        catch (Exception ex) {}
        builder.append('\n').append("Frames : ");
        for (final MessageHandler frame : WakfuGameEntity.getInstance().getFrames()) {
            builder.append('\n').append(" - ").append(frame.getClass().getSimpleName());
        }
        manager.trace(builder.toString());
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
