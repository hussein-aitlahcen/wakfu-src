package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.constants.*;

@Documentation(commandName = "webBrowser | wb", commandParameters = "", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Open a fullscreen browser", commandObsolete = false)
public class WebBrowserCommand extends ModerationCommand
{
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        SWFWrapper.INSTANCE.toggleDisplay(KrosmozGame.FULL_SCREEN_BROWSER);
    }
}
