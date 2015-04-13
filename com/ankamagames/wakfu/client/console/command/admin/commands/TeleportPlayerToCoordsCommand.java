package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "teleport", commandParameters = "&lt;\"pseudo\"&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Teleport on player coords.", commandObsolete = false)
public class TeleportPlayerToCoordsCommand extends ModerationCommand
{
    private final String m_characterNamePattern;
    private final int m_x;
    private final int m_y;
    
    public TeleportPlayerToCoordsCommand(final String characterNamePattern, final ObjectPair<Integer, Integer> coords) {
        super();
        this.m_characterNamePattern = characterNamePattern;
        this.m_x = coords.getFirst();
        this.m_y = coords.getSecond();
    }
    
    @Override
    public boolean isValid() {
        return this.m_characterNamePattern != null;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)31);
        netMessage.addStringParameter(this.m_characterNamePattern);
        netMessage.addIntParameter(this.m_x);
        netMessage.addIntParameter(this.m_y);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
