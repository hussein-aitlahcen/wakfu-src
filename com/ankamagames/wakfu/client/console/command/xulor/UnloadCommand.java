package com.ankamagames.wakfu.client.console.command.xulor;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.*;

public class UnloadCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.isEmpty()) {
            return;
        }
        final String id = args.get(2);
        if (id.equals("all")) {
            Xulor.getInstance().unloadAll();
            return;
        }
        if (Xulor.getInstance().isLoaded(id)) {
            Xulor.getInstance().unload(args.get(2));
        }
        else {
            manager.err(id + " n'est pas charg\u00e9 !");
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
