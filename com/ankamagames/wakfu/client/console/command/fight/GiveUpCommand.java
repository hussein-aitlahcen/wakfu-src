package com.ankamagames.wakfu.client.console.command.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class GiveUpCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        UIMessage.send((short)18000);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
