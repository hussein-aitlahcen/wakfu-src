package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "tpuser", commandParameters = "&lt;\"name\"&gt; &lt;x&gt; &lt;y&gt; &lt;instanceId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Teleport specified user at coords.", commandObsolete = false)
public class TeleportPlayerToInstanceCommand extends ModerationCommand
{
    private final String m_characterNamePattern;
    private final int m_x;
    private final int m_y;
    private final short m_instanceId;
    
    public TeleportPlayerToInstanceCommand(final String characterNamePattern, final ObjectPair<Integer, Integer> coords, final short instanceId) {
        super();
        this.m_characterNamePattern = characterNamePattern;
        this.m_x = coords.getFirst();
        this.m_y = coords.getSecond();
        this.m_instanceId = instanceId;
    }
    
    @Override
    public boolean isValid() {
        return this.m_characterNamePattern != null;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)32);
        netMessage.addStringParameter(this.m_characterNamePattern);
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
