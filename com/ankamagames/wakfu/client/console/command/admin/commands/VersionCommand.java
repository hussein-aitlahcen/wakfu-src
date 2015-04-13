package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.wakfu.common.constants.*;

@Documentation(commandName = "version | v", commandParameters = "", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Show game version.", commandObsolete = false)
public class VersionCommand extends ModerationCommand
{
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        ConsoleManager.getInstance().customTrace(Version.READABLE_DATED_VERSION, 11163033);
    }
}
