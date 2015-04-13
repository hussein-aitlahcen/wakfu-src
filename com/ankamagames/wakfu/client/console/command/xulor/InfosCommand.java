package com.ankamagames.wakfu.client.console.command.xulor;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.*;

public class InfosCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        manager.trace(Xulor.getInstance().getDebugInfos());
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
