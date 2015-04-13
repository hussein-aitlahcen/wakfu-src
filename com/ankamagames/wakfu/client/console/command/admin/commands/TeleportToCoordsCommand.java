package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "teleport | tp", commandParameters = "&lt;x&gt; &lt;y&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Teleport to coords in the same instance.", commandObsolete = false)
public class TeleportToCoordsCommand extends ModerationCommand
{
    private final boolean m_valid;
    private int m_x;
    private int m_y;
    
    public TeleportToCoordsCommand(final ObjectPair<Integer, Integer> coords) {
        super();
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
        return this.m_valid && !AdminRightHelper.checkRights(WakfuGameEntity.getInstance().getLocalPlayer().getAccountInformationHandler().getAdminRights(), AdminRightHelper.NO_RIGHT);
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)6);
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
