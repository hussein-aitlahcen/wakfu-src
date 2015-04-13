package com.ankamagames.baseImpl.client.proxyclient.base.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;

public class NavigateToParentCommandSetCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        manager.navigateToParentCommandDescriptorSet();
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
