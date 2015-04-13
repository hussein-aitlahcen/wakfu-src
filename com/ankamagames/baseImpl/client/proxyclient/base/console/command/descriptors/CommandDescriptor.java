package com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;

public class CommandDescriptor extends CommandPattern
{
    private Command m_command;
    
    public CommandDescriptor(final String cmdRegex, final String argsRegex, final Command command, final boolean allowNoArg) {
        super(cmdRegex, argsRegex, allowNoArg);
        this.m_command = command;
    }
    
    @Override
    public Command createInstance() {
        return this.m_command;
    }
}
