package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.script.*;

public class LoadScriptCommand implements Command
{
    private static LuaScript m_currentScript;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.size() < 4) {
            return;
        }
        final int scriptId = Integer.parseInt(args.get(2));
        final String functionName = args.get(3);
        if (LoadScriptCommand.m_currentScript != null) {
            LoadScriptCommand.m_currentScript.tryToFinish();
        }
        manager.trace("Loading script " + scriptId + "...");
        (LoadScriptCommand.m_currentScript = LuaManager.getInstance().loadScript(scriptId, null, false)).runFunction(functionName);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        LoadScriptCommand.m_currentScript = null;
    }
}
