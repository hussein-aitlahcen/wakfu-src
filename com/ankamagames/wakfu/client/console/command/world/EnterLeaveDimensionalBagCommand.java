package com.ankamagames.wakfu.client.console.command.world;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class EnterLeaveDimensionalBagCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        enterLeaveDimensionalBag();
    }
    
    public static void enterLeaveDimensionalBag() {
        UIMessage.send((short)17013);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
