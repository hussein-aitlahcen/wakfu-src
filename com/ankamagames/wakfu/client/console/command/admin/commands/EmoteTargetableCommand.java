package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "emoteUntargetable | eUntargetable | eu", commandParameters = "&lt;0 | 1&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "eu 1 to be untargetable by emotes. 0 to be targetable", commandObsolete = false)
public class EmoteTargetableCommand extends ModerationCommand
{
    private final int m_targetable;
    
    public EmoteTargetableCommand(final int s) {
        super();
        this.m_targetable = s;
    }
    
    @Override
    public boolean isValid() {
        return this.m_targetable == 1 || this.m_targetable == 0;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.addIntParameter(this.m_targetable);
        netMessage.setCommand((short)198);
        networkEntity.sendMessage(netMessage);
    }
}
