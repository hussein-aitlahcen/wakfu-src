package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;

@Documentation(commandName = "", commandParameters = "", commandRights = {}, commandDescription = "", commandObsolete = false)
public abstract class ModerationCommand
{
    public abstract boolean isValid();
    
    public abstract void execute();
    
    public static void log(final String s) {
        ConsoleManager.getInstance().log(s);
    }
}
