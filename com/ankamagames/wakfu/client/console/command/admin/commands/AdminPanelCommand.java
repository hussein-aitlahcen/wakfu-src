package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

@Documentation(commandName = "", commandParameters = "", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "", commandObsolete = true)
public final class AdminPanelCommand extends ModerationCommand
{
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        WakfuGameEntity.getInstance().pushFrame(UIAdminMonitorFrame.getInstance());
    }
}
