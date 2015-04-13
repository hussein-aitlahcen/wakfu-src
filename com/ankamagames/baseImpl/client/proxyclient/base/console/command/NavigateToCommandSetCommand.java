package com.ankamagames.baseImpl.client.proxyclient.base.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;

public class NavigateToCommandSetCommand implements Command
{
    private CommandDescriptorSet m_commandDescriptorSet;
    
    public NavigateToCommandSetCommand(final CommandDescriptorSet commandDescriptorSet) {
        super();
        this.m_commandDescriptorSet = commandDescriptorSet;
    }
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        manager.setCommandDescriptorSet(this.m_commandDescriptorSet);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
