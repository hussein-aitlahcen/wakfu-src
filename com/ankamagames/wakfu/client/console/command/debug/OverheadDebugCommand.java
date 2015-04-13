package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public class OverheadDebugCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        AnimatedElement.DISPLAY_EXTENDED_OVERHEAD_INFOS = !AnimatedElement.DISPLAY_EXTENDED_OVERHEAD_INFOS;
        if (AnimatedElement.DISPLAY_EXTENDED_OVERHEAD_INFOS) {
            manager.trace("Overhead debug : on");
        }
        else {
            manager.trace("Overhead debug : off");
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
