package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public final class SetPositionCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.size() != 5) {
            return;
        }
        WakfuGameEntity.getInstance().getLocalPlayer().setPosition(Integer.parseInt(args.get(2)), Integer.parseInt(args.get(3)), Short.parseShort(args.get(4)));
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
