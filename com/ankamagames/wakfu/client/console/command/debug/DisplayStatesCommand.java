package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;

public class DisplayStatesCommand implements Command
{
    private static boolean m_forceDisplayStates;
    
    public static boolean forceDisplayStates() {
        return DisplayStatesCommand.m_forceDisplayStates;
    }
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        DisplayStatesCommand.m_forceDisplayStates = !DisplayStatesCommand.m_forceDisplayStates;
        manager.trace(DisplayStatesCommand.m_forceDisplayStates ? "Affichage forc\u00e9 des \u00e9tats : ON" : "Affichage forc\u00e9 des \u00e9tats : OFF");
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        DisplayStatesCommand.m_forceDisplayStates = false;
    }
}
