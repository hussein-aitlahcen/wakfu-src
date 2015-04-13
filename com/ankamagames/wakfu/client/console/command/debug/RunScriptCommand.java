package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.script.*;

public class RunScriptCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.size() < 3) {
            return;
        }
        final int scriptId = Integer.parseInt(args.get(2));
        LuaManager.getInstance().runScript(scriptId);
        manager.trace("Script " + scriptId);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
