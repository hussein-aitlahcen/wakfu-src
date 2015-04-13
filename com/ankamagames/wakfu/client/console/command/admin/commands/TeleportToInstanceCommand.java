package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "teleport | tp", commandParameters = "&lt;x&gt; &lt;y&gt; &lt;instance&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Teleport to coords in another instance", commandObsolete = false)
public class TeleportToInstanceCommand extends ModerationCommand
{
    private final short m_instanceId;
    private int m_x;
    private int m_y;
    private final boolean m_valid;
    
    public TeleportToInstanceCommand(final ObjectPair<Integer, Integer> coords, final short instanceId) {
        super();
        this.m_instanceId = instanceId;
        if (coords != null && coords.getFirst() != null && coords.getSecond() != null) {
            this.m_valid = true;
            this.m_x = coords.getFirst();
            this.m_y = coords.getSecond();
        }
        else {
            this.m_valid = false;
        }
    }
    
    @Override
    public boolean isValid() {
        return this.m_valid;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)22);
        netMessage.addIntParameter(this.m_x);
        netMessage.addIntParameter(this.m_y);
        netMessage.addShortParameter(this.m_instanceId);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
