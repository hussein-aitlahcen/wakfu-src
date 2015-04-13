package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "almanach", commandParameters = "&lt;start&gt; &lt;almanachEventId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Launch Almanach event.", commandObsolete = false)
public final class AlmanachStartEventCommand extends ModerationCommand
{
    private final int m_entryId;
    
    public AlmanachStartEventCommand(final int entryId) {
        super();
        this.m_entryId = entryId;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)131);
        msg.addIntParameter(this.m_entryId);
        networkEntity.sendMessage(msg);
    }
}
